/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax;

/**
 *
 * @author riean
 */
public class ParseError extends Exception {

    public ParseError(String message) {
        super(message);
    }

    public ParseError(Throwable cause) {
        super(cause);
    }

    public ParseError(String message, Throwable cause) {
        super(message, cause);
    }

}
