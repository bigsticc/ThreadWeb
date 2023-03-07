package res.directorytest;

public enum HttpStatus {

    // 1xx Informational
	/** Continue */
	CONTINUE(100, "Continue"),
    /** Switching Protocols */
	SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    /** Processing */
	PROCESSING(102, "Processing"),
    // 2xx Success
    /** OK */
	OK(200, "OK"),
    /** Created */
	CREATED(201, "Created"),
    /** Accepted */
	ACCEPTED(202, "Accepted"),
    /** Non-Authoritative Information */
	NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    /** No Content */
	NO_CONTENT(204, "No Content"),
    /** Reset Content */
	RESET_CONTENT(205, "Reset Content"),
    /** Partial Content */
	PARTIAL_CONTENT(206, "Partial Content"),
    /** Multi-Status */
	MULTI_STATUS(207, "Multi-Status"),
    /** Already Reported */
	ALREADY_REPORTED(208, "Already Reported"),
    /** IM Used */
	IM_USED(226, "IM Used"),
    // 3xx Redirection
    /** Multiple Choices */
	MULTIPLE_CHOICES(300, "Multiple Choices"),
    /** Moved Permanently */
	MOVED_PERMANENTLY(301, "Moved Permanently"),
    /** Found */
	FOUND(302, "Found"),
    /** See Other */
	SEE_OTHER(303, "See Other"),
    /** Not Modified */
	NOT_MODIFIED(304, "Not Modified"),
    /** Use Proxy */
	USE_PROXY(305, "Use Proxy"),
    /** Temporary Redirect */
	TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    /** Permanent Redirect */
	PERMANENT_REDIRECT(308, "Permanent Redirect"),
    // 4xx Client Error
    /** Bad Request */
	BAD_REQUEST(400, "Bad Request"),
    /** Unauthorized */
	UNAUTHORIZED(401, "Unauthorized"),
    /** Payment Required */
	PAYMENT_REQUIRED(402, "Payment Required"),
    /** Forbidden */
	FORBIDDEN(403, "Forbidden"),
    /** Not Found */
	NOT_FOUND(404, "Not Found"),
    /** Method Not Allowed */
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    /** Not Acceptable */
	NOT_ACCEPTABLE(406, "Not Acceptable"),
    /** Proxy Authentication Required */
	PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    /** Request Timeout */
	REQUEST_TIMEOUT(408, "Request Timeout"),
    /** Conflict */
	CONFLICT(409, "Conflict"),
    /** Gone */
	GONE(410, "Gone"),
    /** Length Required */
	LENGTH_REQUIRED(411, "Length Required"),
    /** Precondition Failed */
	PRECONDITION_FAILED(412, "Precondition Failed"),
    /** Payload Too Large */
	PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    /** URI Too Long */
	URI_TOO_LONG(414, "URI Too Long"),
    /** Unsupported Media Type */
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    /** Range Not Satisfiable */
	RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
    /** Expectation Failed */
	EXPECTATION_FAILED(417, "Expectation Failed"),
    /** I'm a Teapot */
	IM_A_TEAPOT(418, "I'm a Teapot"),
    /** Misdirected Request */
	MISDIRECTED_REQUEST(421, "Misdirected Request"),
    /** Unprocessable Entity */
	UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    /** Locked */
	LOCKED(423, "Locked"),
    /** Failed Dependency */
	FAILED_DEPENDENCY(424, "Failed Dependency"),
    /** Upgrade Required */
	UPGRADE_REQUIRED(426, "Upgrade Required"),
    /** Precondition Required */
	PRECONDITION_REQUIRED(428, "Precondition Required"),
    /** Too Many Requests */
	TOO_MANY_REQUESTS(429, "Too Many Requests"),
    /** Request Header Fields Too Large */
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    /** Unavailable For Legal Reasons */
	UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),
    // 5xx Server Error
    /** Internal Server Error */
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /** Not Implemented */
	NOT_IMPLEMENTED(501, "Not Implemented"),
    /** Bad Gateway */
	BAD_GATEWAY(502, "Bad Gateway"),
    /** Service Unavailable */
	SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    /** Gateway Timeout */
	GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    /** HTTP Version Not Supported */
	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    /** Variant Also Negotiates */
	VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    /** Insufficient Storage */
	INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    /** Loop Detected */
	LOOP_DETECTED(508, "Loop Detected"),
    NOT_EXTENDED(510, "Not Extended");


    final int statusCode;
    final String reasonPhrase;
    HttpStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /** Gets the numerical code of the HttpStatus, used for quick identification of the status
     *
     * @return A 3 digit code as defined in RFCs 4918, 8470, 6585, 9110
     */
    public int getStatus() {
        return statusCode;
    }

    /** Gets the default phrase which describes the status code, typically used for generating error pages
     *
     * @return A string containing the default phrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /** Finds HttpStatus by numerical code
     *
     * @param code 3 digit code from 1xx to 5xx
     * @return the HttpStatus value corresponding to the provided code
     */
    public static HttpStatus getStatus(int code) {
        for(HttpStatus status : values()) {
            if(status.getStatus() == code) return status;
        }
        return null;
    }
}
