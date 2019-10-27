package ru.shred.electromachine.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.shred.electromachine.model.ProtocolAzur;
import ru.shred.electromachine.service.ProtocolAzurService;

@Route("azur")
public class AzurUi extends VerticalLayout {

    @Value("${electromachine.ip.address}")
    private String ipAddress;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ProtocolAzurService protocolAzurService;

    private Grid<ProtocolAzur> protocolAzurGrid = new Grid<>(ProtocolAzur.class);

    @Autowired
    public AzurUi(ProtocolAzurService protocolAzurService) {
        this.protocolAzurService = protocolAzurService;

        protocolAzurGrid.setColumns();

        protocolAzurGrid.addColumn(ProtocolAzur::getProtocolNumber).setHeader("Протокол");
        protocolAzurGrid.addColumn(ProtocolAzur::getMeasurementPurpose).setHeader("Цель испытаний");
        protocolAzurGrid.addColumn(ProtocolAzur::getDocumentsNumber).setHeader("Документы");
        protocolAzurGrid.addColumn(ProtocolAzur::getResultVisualInspection).setHeader("Результат визуального осмотра");
        protocolAzurGrid.addColumn(ProtocolAzur::getClimateData).setHeader("Климатические данные");
        protocolAzurGrid.addColumn(
                new NativeButtonRenderer<>("Рез-ты испытаний",
                        clickedItem -> {
                            UI.getCurrent().getPage().setLocation(
                                    String.format("%s:%s%s/azur/test?protocol-id=%s", ipAddress, port, contextPath, clickedItem.getId()));
                        })
        );
        protocolAzurGrid.addColumn(
                new NativeButtonRenderer<>("Удалить",
                        clickedItem -> {
                            deleteAzur(clickedItem.getId());
                        })
        );

        protocolAzurGrid.getColumns().forEach(column -> column.setSortable(true));
        protocolAzurGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        protocolAzurGrid.setItems(protocolAzurService.getAll());

        add(protocolAzurGrid);
    }

    private void deleteAzur(Long id) {
        protocolAzurService.delete(id);
    }
}
