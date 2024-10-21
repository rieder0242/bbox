/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax;

/**
 *
 * @author riean
 */
public interface Control {

    public int read() throws ParseError;

    public void setState(StateKind stateKind);

    public void inc();

    public void dec();

}
