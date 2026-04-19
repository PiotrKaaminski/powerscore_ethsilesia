package pl.kaminski.powerscore.client.query.contract;

import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

import java.util.UUID;

public class GetClientResult extends Result<ClientInfo, GetClientResult.Error> {

    private GetClientResult(ClientInfo clientInfo) {super(clientInfo);}
    private GetClientResult(Error error) {super(error);}

    public static GetClientResult success(ClientInfo clientInfo) {return new GetClientResult(clientInfo);}
    public static GetClientResult userNotFound(UUID id) {return new GetClientResult(new UserNotFound(id));}

    public sealed interface Error extends ResultError { }
    public record UserNotFound(UUID id) implements Error {
        @Override
        public String getMessage() {
            return "User with given id not found";
        }
    }
}
