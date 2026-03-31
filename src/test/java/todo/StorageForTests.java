package todo;

import java.util.ArrayList;
import java.util.List;

public class StorageForTests extends FileStorage {
    @Override
    public void save(List<Task> tasks){

    }

    @Override
    public List<Task> load(){
        return new ArrayList<>();
    }


}
