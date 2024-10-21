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
public class CloseTegMedState extends State {

    public CloseTegMedState(Control control) {
        super(control);
    }

    @Override
    public void step(Symbol symbol) throws ParseError {
        switch (symbol) {
            case TAG_END -> {
                control.setState(StateKind.TEXT);
                control.dec();
            }
            case QUOTATION_MARK ->
                throw new ParseError("Invalid quotation mark (\") in close tag.");                
            case SLESH ->
                throw new ParseError("Invalid slesh character (\\) in close tag.");
            default -> {
                //NOP
            }
        }
    }

}
