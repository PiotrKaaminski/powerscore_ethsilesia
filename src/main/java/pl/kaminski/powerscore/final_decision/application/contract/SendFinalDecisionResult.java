package pl.kaminski.powerscore.final_decision.application.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.EmptyResult;
import pl.kaminski.powerscore.commons.result.ResultError;

public class SendFinalDecisionResult extends EmptyResult<SendFinalDecisionResult.Error> {
    private SendFinalDecisionResult() {super();}
    private SendFinalDecisionResult(Error error) {super(error);}
    public static SendFinalDecisionResult success() {return new SendFinalDecisionResult();}
    public static SendFinalDecisionResult error(Error error) {return new SendFinalDecisionResult(error);}

    public static SendFinalDecisionResult finalDecisionNotFound() {
        return new SendFinalDecisionResult(new FinalDecisionNotFound());
    }

    public static SendFinalDecisionResult invalidInputData() {
        return new SendFinalDecisionResult(new InvalidInputData());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static SendFinalDecisionResult finalDecisionAlreadySent() {
        return new SendFinalDecisionResult(new FinalDecisionAlreadySent());
    }

    public sealed interface Error extends ResultError { }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public record FinalDecisionNotFound() implements Error {
        @Override
        public String getMessage() {
            return "Final decision not found";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record InvalidInputData() implements Error {
        @Override
        public String getMessage() {
            return "Invalid input data";
        }
    }

    public record FinalDecisionAlreadySent() implements Error {
        @Override
        public String getMessage() {
            return "Final decision already sent";
        }
    }

}
