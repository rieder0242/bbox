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
public class OutState extends State {

    private final boolean enableNonWhiteSpace;

    public OutState(Control control, boolean enableNonWhiteSpace) {
        super(control);
        this.enableNonWhiteSpace = enableNonWhiteSpace;
    }

    @Override
    public void step(Symbol symbol) throws ParseError {
        switch (symbol) {
            case WHITE_SPACE -> {
                // NOP
                break;
            }
            case TAG_BEGIN -> {
                control.setState(StateKind.TAG_BEGIN);
                break;
            }
            default -> {
                if (!enableNonWhiteSpace) {
                    throw new ParseError("Invalid character (" + symbol.name() + ") before xml");
                }
            }
        }
    }

}
