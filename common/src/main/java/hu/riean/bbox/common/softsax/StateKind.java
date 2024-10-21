/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.softsax;

/**
 *
 * @author riean
 */
public enum StateKind {
    PRE_XML,
    TAG_BEGIN,
    TEXT,
    SELF_CLOSING_TAG,
    OPEN_TAG_MID,
    CLOSE_TAG_MID,
    QUOTATION;
}
