/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax.state;

import hu.riean.bbox.common.softsax.Control;
import hu.riean.bbox.common.softsax.ParseError;
import hu.riean.bbox.common.softsax.Symbol;

/**
 *
 * @author riean
 */
public abstract class State {

    Control control;

    public State(Control control) {
        this.control = control;
    }

    private Symbol readSymbol() throws ParseError {
        int readed = control.read();
        if (readed > 127) {// utf-8
            control.read();
            if (readed > 127 + 64) {
                control.read();
                if (readed > 127 + 64 + 32) {
                    control.read();
                }
            }
            return Symbol.UTF8_LONG;
        }
        if ((readed >= 'a' && readed <= 'z') || (readed >= 'A' && readed <= 'Z') || (readed >= '0' && readed <= '9')) {
            return Symbol.ALPAH_NUMERIC;
        }
        if ((readed >= 9 && readed <= 13) || readed == ' ') {
            return Symbol.WHITE_SPACE;
        }
        return switch (readed) {
            case '<' ->
                Symbol.TAG_BEGIN;
            case '>' ->
                Symbol.TAG_END;
            case '/' ->
                Symbol.SLESH;
            case '"' ->
                Symbol.QUOTATION_MARK;
            case '!' ->
                Symbol.EXCLAMATION_MARK;
            case '?' ->
                Symbol.QUESTION_MARK;
            case '-' ->
                Symbol.HYPHEN;
            default ->
                Symbol.OTHER;
        };
    }

    public void step() throws ParseError {
        Symbol symbol = readSymbol();
        step(symbol);
    }

    abstract void step(Symbol symbol) throws ParseError;

}
