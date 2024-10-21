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
public class QuotationState extends State {
    
    public QuotationState(Control control) {
        super(control);
    }
    
    @Override
    void step(Symbol symbol) throws ParseError {
        if (symbol == Symbol.QUOTATION_MARK) {
            control.setState(StateKind.OPEN_TAG_MID);
        }
    }
    
}
