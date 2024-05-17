package pt.ulisboa.tecnico.gardenmanager.constants;

public class ErrorMessages {
    public static final String VOLLEY_ERROR = "Volley error";
    public static final String NODE_NOT_FOUND = "Node not found";
    public static final String JSON_ERROR = "Json error";
    public static final String MESSAGE_NOT_FOUND = "Message not found";
    public static final String NO_MESSAGES_FOUND_FOR_NODE = "No messages found for node";
    public static final String UNKNOWN_SERFVER_ERROR = "Unknown error";
    public static final String UNKNOWN_ERROR = "Unknown error";

    public static String getErrorMessage(int statusCode) {
        switch(statusCode) {
            case StatusCodes.NO_MESSAGES_FOUND_FOR_NODE:
                return NO_MESSAGES_FOUND_FOR_NODE;
            default:
                return UNKNOWN_SERFVER_ERROR;
        }
    }
}
