package org.jorgecardoso.purewidgets.demo.everybodyvotes.client;

/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Shows one panel at a time, sliding them left or right based on the order they
 * were added. A full fledged version might implement
 * {@link com.google.gwt.user.client.ui.InsertPanel.ForIsWidget}.
 * <p>
 * Note that we implement HasWidgets so that SlidingPanel will work nicely in
 * ui.xml files.
 */
public class SlidingPanel extends ResizeComposite implements HasWidgets,
    HasOneWidget {

  private int currentIndex = -1;
  private final LayoutPanel layoutPanel = new LayoutPanel();
  private final List<Widget> widgets = new ArrayList<Widget>();

  public SlidingPanel() {
    initWidget(layoutPanel);
  }

  public void add(IsWidget w) {
    add(w.asWidget());
  }

  @Override
public void add(Widget w) {
    widgets.remove(w);
    widgets.add(w);

    // Display the first widget added by default
    if (currentIndex < 0) {
      layoutPanel.add(w);
      currentIndex = 0;
    }
  }

  @Override
public void clear() {
    setWidget(null);
    widgets.clear();
  }

  @Override
public Widget getWidget() {
    return widgets.get(currentIndex);
  }

  /**
 * @return the widgets
 */
public List<Widget> getWidgets() {
	return widgets;
}

  @Override
public Iterator<Widget> iterator() {
    return Collections.unmodifiableList(widgets).iterator();
  }

  @Override
public boolean remove(Widget w) {
    return widgets.remove(w);
  }

  @Override
public void setWidget(IsWidget w) {
    setWidget(w.asWidget());
  }

  // Conflict btw deprecated Composite#setWidget and HasOneWidget#setWidget
  @Override
  public void setWidget(Widget widget) {
    int newIndex = widgets.indexOf(widget);

    if (newIndex < 0) {
      newIndex = widgets.size();
      add(widget);
    }

    show(newIndex);
  }

private void show(int newIndex) {
	
	/*
	 * If the browser tab is not showing it seems that we are not getting the onAnimationComplete,
	 * so we verify again if the old widget was removed before starting a new animation.
	 */
	while ( layoutPanel.getWidgetCount() > 1 ) {
		layoutPanel.remove(0);
	}
	
    if (newIndex == currentIndex) {
      return;
    }

    boolean fromLeft = newIndex < currentIndex;
    currentIndex = newIndex;

    Widget widget = widgets.get(newIndex);
    final Widget current = layoutPanel.getWidget(0);

    // Initialize the layout.
    layoutPanel.add(widget);
    layoutPanel.setWidgetLeftWidth(current, 0, Unit.PCT, 100, Unit.PCT);
    if (fromLeft) {
      layoutPanel.setWidgetLeftWidth(widget, -100, Unit.PCT, 100, Unit.PCT);
    } else {
      layoutPanel.setWidgetLeftWidth(widget, 100, Unit.PCT, 100, Unit.PCT);
    }
    layoutPanel.forceLayout();

    // Slide into view.
    if (fromLeft) {
      layoutPanel.setWidgetLeftWidth(current, 100, Unit.PCT, 100, Unit.PCT);
    } else {
      layoutPanel.setWidgetLeftWidth(current, -100, Unit.PCT, 100, Unit.PCT);
    }
    layoutPanel.setWidgetLeftWidth(widget, 0, Unit.PCT, 100, Unit.PCT);
    layoutPanel.animate(500, new Layout.AnimationCallback() {
      @Override
	public void onAnimationComplete() {
        // Remove the old widget when the animation completes.
        layoutPanel.remove(current);
      }

      @Override
	public void onLayout(Layer layer, double progress) {
      }
    });
  }
}
