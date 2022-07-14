package com.printnode.api;

import org.apache.commons.codec.binary.Base64;

import java.net.URL;

/**
 * An object to be serialized into JSON for creating printjobs.
 * */
public class PrintJobJson {
    /**
     * The id of the printer we're printing on.
     * */
    private int printerId;
    /**
     * The title of the PrintJob.
     * */
    private String title;
    /**
     * The type of content we are using.
     * */
    private String contentType;
    /**
     * If content-type xxx_base64:
     * This is a path to the file you want to upload.
     * If content-type xxx_uri:
     * This is a URL to the file you want printed.
     * */
    private String content;
    /**
     * Source of the PrintJob.
     * */
    private String source;
    /**
     * Options to be set on this PrintJob.
     * Add these via PrintJobJson.getOptions.set(option)..
     * */
    private Options options;
    /**
     * Set time to expire on this PrintJob.
     * */
    private int expireAfter = -1;
    /**
     * How many times you want to send this PrintJob.
     * */
    private int qty = 1;

    /**
     * Creates an object to be serialized into JSON.
     *
     * @param printerId   id of the printer which wil run the PrintJob.
     * @param title       title of the PrintJob.
     * @param contentType Type of content. base64, uri, etc.
     * @param content     either a file, or a URL to a file. Depends on contentType.
     * @param source      A text description of how the print job was created or where the print job originated.
     **/
    private PrintJobJson(final int printerId,
                         final String title,
                         final String contentType,
                         final String content,
                         final String source) {
        this.printerId = printerId;
        this.title = title;
        this.contentType = contentType;
        this.content = content;
        this.source = source;
        options = new Options();
    }

    /**
     * Creates an object to be serialized into JSON.
     *
     * @param printerId id of the printer which wil run the PrintJob.
     * @param title     title of the PrintJob.
     * @param content   either a file, or a URL to a file. Depends on contentType.
     * @param source    A text description of how the print job was created or where the print job originated.
     **/
    public static PrintJobJson ofPdfContent(final int printerId,
                                            final String title,
                                            final byte[] content,
                                            final String source) {
        return new PrintJobJson(printerId, title, "pdf_base64", new String(Base64.encodeBase64(content)), source);
    }

    /**
     * Creates an object to be serialized into JSON.
     *
     * @param printerId id of the printer which wil run the PrintJob.
     * @param title     title of the PrintJob.
     * @param uri       content URI
     * @param source    A text description of how the print job was created or where the print job originated.
     **/
    public static PrintJobJson ofPdfUri(final int printerId,
                                        final String title,
                                        final URL uri,
                                        final String source) {
        return new PrintJobJson(printerId, title, "pdf_uri", uri.toString(), source);
    }

    /**
     * @return Options object of this PrintJob.
     * */
    public final Options getOptions() {
        return options;
    }

    /**
     * @param newExpireAfter set the timeout for this PrintJob.
     * */
    public final void setExpireAfter(final int newExpireAfter) {
        expireAfter = newExpireAfter;
    }

    /**
     * @param newQty set the number of times this PrintJob is sent to the queue.
     * */
    public final void setQty(final int newQty) {
        qty = newQty;
    }

}
