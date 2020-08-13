package de.kaiserpfalzedv.rpg.admin.dao;

import io.quarkus.arc.Unremovable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author kostenko
 */
@Unremovable
@ApplicationScoped
public class DataAccessService {

    public static List<MockData> getRoundsCountHistory() {
        List<MockData> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            LinkedHashMap<String, Long> data = new LinkedHashMap<>();
            data.put("2020-01", Long.valueOf(44 * i));
            data.put("2020-02", Long.valueOf(94 * i));
            data.put("2020-03", Long.valueOf(43 * i));
            data.put("2020-04", Long.valueOf(32 * i));
            data.put("2020-05", Long.valueOf(94 * i));
            data.put("2020-06", Long.valueOf(34 * i));
            data.put("2020-07", Long.valueOf(42 * i));
            data.put("2020-08", Long.valueOf(49 * i));
            data.put("2020-09", Long.valueOf(64 * i));
            data.put("2020-10", Long.valueOf(11 * i));
            data.put("2020-11", Long.valueOf(67 * i));
            data.put("2020-12", Long.valueOf(42 * i));

            MockData history = new MockData();
            history.setCustomer("Customer" + i);
            history.setDataMap(data);
            list.add(history);
        }
        return list;
    }

}
