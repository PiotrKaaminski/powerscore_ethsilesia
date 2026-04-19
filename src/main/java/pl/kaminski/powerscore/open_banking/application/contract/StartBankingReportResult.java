package pl.kaminski.powerscore.open_banking.application.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.EmptyResult;
import pl.kaminski.powerscore.commons.result.ResultError;

public class StartBankingReportResult extends EmptyResult<StartBankingReportResult.Error> {

    private StartBankingReportResult() {
        super();
    }

    private StartBankingReportResult(Error error) {
        super(error);
    }

    public static StartBankingReportResult success() {
        return new StartBankingReportResult();
    }

    public static StartBankingReportResult error(Error error) {
        return new StartBankingReportResult(error);
    }

    public static StartBankingReportResult invalidInputData() {
        return new StartBankingReportResult(new InvalidInputData());
    }

    public static StartBankingReportResult openBankingReportNotFound() {
        return new StartBankingReportResult(new OpenBankingReportNotFound());
    }

    public sealed interface Error extends ResultError {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record InvalidInputData() implements Error {
        @Override
        public String getMessage() {
            return "Invalid input data";
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public record OpenBankingReportNotFound() implements Error {
        @Override
        public String getMessage() {
            return "Open banking report not found";
        }
    }
}
