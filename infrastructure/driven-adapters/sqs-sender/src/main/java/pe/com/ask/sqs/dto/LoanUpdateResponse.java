package pe.com.ask.sqs.dto;

public record LoanUpdateResponse(Response response) {
    public record Response(
            String loanApplicationId,
            String status
    ) {}
}