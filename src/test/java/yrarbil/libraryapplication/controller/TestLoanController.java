package yrarbil.libraryapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yrarbil.libraryapplication.model.Loan;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.model.enums.LoanStatus;
import yrarbil.libraryapplication.service.LoanService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TestLoanController {

    @Mock
    private LoanService loanService;
    @InjectMocks
    private LoanController loanController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    public void testGetAllLoans() throws Exception {

        Loan loan1 = new Loan(new Patron(), LocalDate.parse("2023-02-02"), LocalDate.parse("2023-03-02"), null, LoanStatus.ACTIVE);

        Loan loan2 = new Loan(new Patron(), LocalDate.parse("2023-01-01"), LocalDate.parse("2023-02-01"), LocalDate.parse("2023-01-20"), LoanStatus.RETURNED);

        List<Loan> loanList = Arrays.asList(loan1, loan2);

        Mockito.when(loanService.getAllLoans()).thenReturn(loanList);

        mockMvc.perform(MockMvcRequestBuilders.get("/loan"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void testGetLoanById() throws Exception {
        Loan loan = new Loan();
        when(loanService.getLoanById(1L)).thenReturn(Optional.of(loan));

        mockMvc.perform(get("/loan/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loan)));
    }

    @Test
    public void testGetLoanByNonExistentId () throws Exception {
        when (loanService.getLoanById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/loan/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddLoan() throws Exception {
        Loan loan = new Loan(new Patron(), LocalDate.parse("2023-02-02"), LocalDate.parse("2023-03-02"), null, LoanStatus.ACTIVE);

        when(loanService.save(any(Loan.class))).thenReturn(loan);

        mockMvc.perform(post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(loan)));
    }

    @Test
    public void testAddInvalidLoan() throws Exception {
        Loan invalidLoan = new Loan();
        invalidLoan.setPatron(null);

        mockMvc.perform(post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoan)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteLoan() throws Exception {
        mockMvc.perform(delete("/loan/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistentLoan() throws Exception {
        doThrow(new EntityNotFoundException()).when(loanService).delete(anyLong());

        mockMvc.perform(delete("/loan/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateLoan() throws Exception {
        Loan loan = new Loan(new Patron(), LocalDate.parse("2023-02-02"), LocalDate.parse("2023-03-02"), null, LoanStatus.ACTIVE);

        when(loanService.updateById(1L, loan)).thenReturn(Optional.of(loan));

        mockMvc.perform(put("/loan/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loan)));
    }

    @Test
    public void testUpdateNonExistentLoan() throws Exception {
        Loan loan = new Loan(new Patron(), LocalDate.parse("2023-02-02"), LocalDate.parse("2023-03-02"), null, LoanStatus.ACTIVE);

        when(loanService.updateById(anyLong(), any(Loan.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/loan/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isNotFound());
    }
}
