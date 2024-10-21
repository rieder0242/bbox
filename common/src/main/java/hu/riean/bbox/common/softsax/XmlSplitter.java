/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax;

import hu.riean.bbox.common.softsax.state.CloseTegMedState;
import hu.riean.bbox.common.softsax.state.State;
import hu.riean.bbox.common.softsax.state.OutState;
import hu.riean.bbox.common.softsax.state.QuotationState;
import hu.riean.bbox.common.softsax.state.OpenTegMedState;
import hu.riean.bbox.common.softsax.state.SelfCloseTegMedState;
import hu.riean.bbox.common.softsax.state.TagBeginState;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author riean
 */
public class XmlSplitter {

    InputStream inputStream;
    State state;
    ByteArrayOutputStream copy;
    int deep;
    boolean next;
    Map<StateKind, State> states = new HashMap<>();

    public XmlSplitter(InputStream inputStream) {
        this.inputStream = inputStream;
        states.put(StateKind.PRE_XML, new OutState(control, false));
        states.put(StateKind.TAG_BEGIN, new TagBeginState(control));
        states.put(StateKind.OPEN_TAG_MID, new OpenTegMedState(control));
        states.put(StateKind.CLOSE_TAG_MID, new CloseTegMedState(control));
        states.put(StateKind.TEXT, new OutState(control, true));
        states.put(StateKind.QUOTATION, new QuotationState(control));
        states.put(StateKind.SELF_CLOSING_TAG, new SelfCloseTegMedState(control));
    }

    public InputStream next() throws ParseError {
        try {
            state = states.get(StateKind.PRE_XML);
            copy = new ByteArrayOutputStream();
            deep = 0;
            next = true;
            while (next) {
                state.step();
            }
            final ByteArrayInputStream ret = new ByteArrayInputStream(copy.toByteArray());
            return ret;
        } catch (ParseError ex) {
            throw new ParseError("Error in :" + new String(copy.toByteArray()), ex);
        } finally {
            copy = null;
        }
    }

    Control control = new Control() {
        @Override
        public int read() throws ParseError {
            try {
                int readed = inputStream.read();
                if (readed == -1) {
                    throw new ParseError("Premature end of stream.");
                }
                copy.write(readed);
                return readed;
            } catch (IOException ex) {
                throw new ParseError(ex);
            }
        }

        @Override
        public void setState(StateKind stateKind) {
            assert stateKind != null : "stateKind is null";
            state = states.get(stateKind);
            assert state != null : "state is null at " + stateKind;
        }

        @Override
        public void inc() {
            deep++;
        }

        @Override
        public void dec() {
            deep--;
            next = deep > 0;// ha van még kijebbi tag tovább olvaunk
        }
    };

}
