package com.anthem.platform.core.beans;

/**
 * The enum Http method type.
 *
 * @author aswaroop
 */
public enum HttpMethodType {

    /**
     * select
     */
    GET,

    /**
     * edit
     */
    POST,

    /**
     * add
     */
    PUT,

    /**
     * update
     */
    PATCH,

    /**
     * DELETE
     */
    DELETE;

	@Override
    public String toString() {
        return name();
    }
}
