/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax.state;

import hu.riean.bbox.common.softsax.Control;
import hu.riean.bbox.common.softsax.ParseError;
import hu.riean.bbox.common.softsax.StateKind;
import hu.riean.bbox.common.softsax.Symbol;
import static hu.riean.bbox.common.softsax.Symbol.TAG_END;

/**
 *
 * @author riean
 */
public class SelfCloseTegMedState extends State {

    public SelfCloseTegMedState(Control control) {
        super(control);
    }

    @Override
    public void step(Symbol symbol) throws ParseError {
        switch (symbol) {
            case WHITE_SPACE -> {
            }
            case TAG_END -> {
                control.setState(StateKind.TEXT);
                control.dec();
            }
            default -> {
                throw new ParseError("Invalid character (" + symbol + ") in self close tag.");
                //NOP
            }
        }
    }

}
