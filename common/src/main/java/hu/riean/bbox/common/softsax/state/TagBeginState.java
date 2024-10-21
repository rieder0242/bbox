/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax.state;

import hu.riean.bbox.common.softsax.Control;
import hu.riean.bbox.common.softsax.ParseError;
import hu.riean.bbox.common.softsax.StateKind;
import hu.riean.bbox.common.softsax.Symbol;

/**
 *
 * @author riean
 */
public class TagBeginState extends State {

    public TagBeginState(Control control) {
        super(control);
    }

    @Override
    public void step(Symbol symbol) throws ParseError {
        switch (symbol) {
            case WHITE_SPACE -> {
                // NOP
            }
            case ALPAH_NUMERIC -> {
                control.setState(StateKind.OPEN_TAG_MID);
                control.inc();
            }
            case SLESH ->
                control.setState(StateKind.CLOSE_TAG_MID);
            case EXCLAMATION_MARK ->
                throw new UnsupportedOperationException("Not supported yet.");
            case QUOTATION_MARK ->
                throw new UnsupportedOperationException("Not supported yet.");
            default ->
                throw new ParseError("Invalid character (" + symbol + ") after tag begin.");
        }
    }

}
