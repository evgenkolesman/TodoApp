package todolist.data;

import org.junit.Ignore;
import org.junit.Test;
import todolist.model.Category;
import todolist.model.Item;
import todolist.model.User;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class StoreDataTest {

    @Test
    @Ignore
    public void updateTest() throws SQLException {
        StoreData sd = StoreData.getInstance();
        Item item = new Item("desc", new User(), new ArrayList<Category>());
        sd.add(item);
        sd.update(item.getId(), true);
        assertThat(sd.findById(item.getId()).getDone(), is(true));
        sd.delete(item.getId());
    }

    @Test
    @Ignore
    public void deleteAndAddTest() throws SQLException {
        StoreData sd = StoreData.getInstance();
        Item item = new Item("desc", new User(), new ArrayList<Category>());
        sd.add(item);
        sd.delete(item.getId());
        assertNull(sd.findById(item.getId()));
    }
}