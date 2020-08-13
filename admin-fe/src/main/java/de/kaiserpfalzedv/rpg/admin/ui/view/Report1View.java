package de.kaiserpfalzedv.rpg.admin.ui.view;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.inject.spi.CDI;
import de.kaiserpfalzedv.rpg.admin.dao.DataAccessService;
import de.kaiserpfalzedv.rpg.admin.dao.MockData;
import de.kaiserpfalzedv.vaadin.BeanNavigatorView;


/**
 *
 * @author kostenko
 */
@BeanNavigatorView(name="report1", icon = "TABLE", weight = 2)
public class Report1View extends VerticalLayout implements View {

    DataAccessService dao = CDI.current().select(DataAccessService.class).get();

    public Report1View() {
        setSizeFull();
        addStyleName("transactions");
        setMargin(false);
        setSpacing(false);
        addComponent(buildToolbar());
        Grid grid = buildGrid();
        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    private Grid buildGrid() {
        Grid<MockData> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();

        List<MockData> historyList = dao.getRoundsCountHistory();
        ListDataProvider<MockData> dataProvider = DataProvider.ofCollection(historyList);
        grid.setDataProvider(dataProvider);

        grid.addColumn(MockData::getCustomer).setId("CustomerColumn").setCaption("Company");

        for (String key : historyList.get(0).getDataMap().keySet()) {
            grid.addColumn(h -> h.getDataMap().get(key))
                    .setId(key)
                    .setCaption(key);
        }

        // Add a summary footer row to the Grid
        FooterRow footer = grid.appendFooterRow();
        // Update the summary row every time data has changed
        // by collecting the sum of each column's data
        grid.getDataProvider().addDataProviderListener(event -> {
            List<MockData> data = event.getSource().fetch(new Query<>()).collect(Collectors.toList());

            for (String key : historyList.get(0).getDataMap().keySet()) {
                Long c = data.stream()
                        .map(h -> h.getDataMap().get(key))
                        .reduce(0l, Long::sum);

                footer.getCell(key).setText(c.toString());
            }
        });
        // Fire a data change event to initialize the summary footer
        grid.getDataProvider().refreshAll();
        // Allow column reordering
        grid.setColumnReorderingAllowed(true);
        // Allow column hiding
        grid.getColumns().stream().forEach(column -> column.setHidable(true));

        return grid;
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label title = new Label("Report #1");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        DateField df = new DateField();
        df.setPlaceholder("Date from");

        DateField dt = new DateField();
        dt.setPlaceholder("Date to");

        final Button button = new Button("Export");

        HorizontalLayout tools = new HorizontalLayout(df, dt, button);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

}
