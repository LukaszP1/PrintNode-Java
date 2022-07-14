package com.printnode.api;

public class PrintNodeException extends RuntimeException {

    /**
     * @inheritDoc
     */
    public PrintNodeException() {
        super();
    }

    /**
     * @inheritDoc
     */
    public PrintNodeException(String message) {
        super(message);
    }

    /**
     * @inheritDoc
     */
    public PrintNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @inheritDoc
     */
    public PrintNodeException(Throwable cause) {
        super(cause);
    }


}
