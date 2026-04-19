package pl.kaminski.powerscore.verification.application.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.EmptyResult;
import pl.kaminski.powerscore.commons.result.ResultError;

public class HandleBankingReportApprovalResult extends EmptyResult<HandleBankingReportApprovalResult.Error> {
    private HandleBankingReportApprovalResult() {super();}
    private HandleBankingReportApprovalResult(Error error) {super(error);}

    public static HandleBankingReportApprovalResult success() {return new HandleBankingReportApprovalResult();}
    public static HandleBankingReportApprovalResult error(Error error) {return new HandleBankingReportApprovalResult(error);}

    public static HandleBankingReportApprovalResult verificationNotFound() {return new HandleBankingReportApprovalResult(new VerificationNotFound());}
    public static HandleBankingReportApprovalResult bankingReportApprovalAlreadySent() {return new HandleBankingReportApprovalResult(new BankingReportApprovalAlreadySent());}
    public static HandleBankingReportApprovalResult bankingStepNotInProgress() {return new HandleBankingReportApprovalResult(new BankingStepNotInProgress());}
    public static HandleBankingReportApprovalResult bankingReportClientApprovalEmpty() {return new HandleBankingReportApprovalResult(new BankingReportClientApprovalEmpty());}


    public sealed interface Error extends ResultError { }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public record VerificationNotFound() implements Error {
        @Override
        public String getMessage() {
            return "Verification not found";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record BankingReportApprovalAlreadySent() implements Error {
        @Override
        public String getMessage() {
            return "Banking report approval already sent";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record BankingStepNotInProgress() implements Error {
        @Override
        public String getMessage() {
            return "Banking step is not in progress";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record BankingReportClientApprovalEmpty() implements Error {
        @Override
        public String getMessage() {
            return "Banking report approval is empty";
        }
    }

}
