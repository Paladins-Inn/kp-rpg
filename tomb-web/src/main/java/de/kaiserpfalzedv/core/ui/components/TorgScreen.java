/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.core.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;

import static com.vaadin.flow.component.Unit.PERCENTAGE;
import static com.vaadin.flow.component.Unit.PIXELS;

/**
 * TorgScreen -- A generic full screen in TORG design.
 * <p>
 * You may use the default methods for handling the center column and have special methods to handle the left and right
 * column if needed.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-04-18
 */
@SuppressWarnings("ALL")
@Sfl4j
public class TorgScreen extends Div {
    private final VerticalLayout left, center, right;

    public TorgScreen() {
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        left = generateLeftBorder();
        right = generateRightBorder();
        center = generateContentBox();
        center.setWidth(25, PERCENTAGE);

        layout.add(left, center, right);
        layout.setFlexGrow(2, left);
        layout.setFlexGrow(1, center);
        layout.setFlexGrow(1, right);

        super.add(layout);
    }

    private VerticalLayout generateRightBorder() {
        final VerticalLayout right;
        right = generateBorder();
        right.setWidth(25, PERCENTAGE);
        return right;
    }

    private VerticalLayout generateLeftBorder() {
        final VerticalLayout left;
        left = generateBorder();
        left.setWidth(50, PERCENTAGE);
        return left;
    }

    private VerticalLayout generateBorder() {
        VerticalLayout result = new VerticalLayout();

        result.setClassName("torg-marble");
        result.setHeightFull();

        Div space = new Div();
        space.setMinWidth(10, PIXELS);
        space.setMaxWidth(50, PERCENTAGE);
        result.add(space);

        return result;
    }

    private VerticalLayout generateContentBox() {
        VerticalLayout result = new VerticalLayout();
        result.setHeightFull();
        result.setMinWidth(10, PIXELS);
        result.setMaxWidth(25, PERCENTAGE);

        return result;
    }

    @Override
    public void add(Component... components) {
        log.debug("Adding center components: {}", (Object[]) components);

        center.add(components);
    }

    @Override
    public void add(String text) {
        log.debug("Adding center text: '{}'", text);

        center.add(text);
    }

    @Override
    public void remove(Component... components) {
        log.debug("Removing center components: {}", (Object[]) components);

        center.remove(components);
    }

    @Override
    public void removeAll() {
        log.debug("Removing all center components.");

        center.removeAll();
    }

    @Override
    public void addComponentAtIndex(int index, Component component) {
        log.debug("Add center component. index={}, component={}", index, component);

        center.addComponentAtIndex(index, component);
    }

    @Override
    public void addComponentAsFirst(Component component) {
        log.debug("Add center component as first. component={}", component);

        center.addComponentAsFirst(component);
    }


    public void addInLeftBorder(Component... components) {
        log.debug("Adding left components: {}", (Object[]) components);

        left.add(components);
    }

    public void addInLeftBorder(String text) {
        log.debug("Adding left text: '{}'", text);

        left.add(text);
    }

    public void removeFromLeftBorder(Component... components) {
        log.debug("Removing left components: {}", (Object[]) components);

        left.remove(components);
    }

    public void removeAllFromLeftBorder() {
        log.debug("Removing all left components.");

        left.removeAll();
    }

    public void addComponentAtIndexInLeftBorder(int index, Component component) {
        log.debug("Add left component. index={}, component={}", index, component);

        left.addComponentAtIndex(index, component);
    }

    public void addComponentAsFirstInLeftBorder(Component component) {
        log.debug("Add left component as first. component={}", component);

        left.addComponentAsFirst(component);
    }


    public void addInRightBorder(Component... components) {
        log.debug("Adding right components: {}", (Object[]) components);

        right.add(components);
    }

    public void addInRightBorder(String text) {
        log.debug("Adding right text: '{}'", text);

        right.add(text);
    }

    public void removeFromRightBorder(Component... components) {
        log.debug("Removing right components: {}", (Object[]) components);

        right.remove(components);
    }

    public void removeAllFromRightBorder() {
        log.debug("Removing all right components.");

        right.removeAll();
    }

    public void addComponentAtIndexInRightBorder(int index, Component component) {
        log.debug("Add right component. index={}, component={}", index, component);

        right.addComponentAtIndex(index, component);
    }

    public void addComponentAsFirstInRightBorder(Component component) {
        log.debug("Add right component as first. component={}", component);

        right.addComponentAsFirst(component);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", TorgScreen.class.getSimpleName() + "[", "]")
                .add("center=" + center.getChildren())
                .add("left=" + left.getChildren())
                .add("right=" + right.getChildren())
                .toString();
    }
}