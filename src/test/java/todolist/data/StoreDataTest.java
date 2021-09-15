package todolist.data;

import org.junit.Test;
import todolist.model.Item;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class StoreDataTest {

    @Test
    public void updateTest() throws SQLException {
        StoreData sd = new StoreData();
        Item item = new Item("desc");
        sd.add(item);
        sd.update(item.getId(), true);
        assertThat(sd.findById(item.getId()).getDone(), is(true));
        sd.delete(item.getId());
    }

    @Test
    public void deleteAndAddTest() throws SQLException {
        StoreData sd = new StoreData();
        Item item = new Item("desc");
        sd.add(item);
        sd.delete(item.getId());
        assertNull(sd.findById(item.getId()));
    }
}