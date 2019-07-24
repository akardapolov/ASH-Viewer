package store;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pojo.SqlColMetadata;
import store.entity.database.MainData;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Singleton
public class RawStoreManager {
    private StoreManager storeManager;
    private ConvertManager convertManager;

    @Getter @Setter private List<SqlColMetadata> sqlColMetadata;

    @Inject
    public RawStoreManager(StoreManager storeManager,
                           ConvertManager convertManager) {
        this.storeManager = storeManager;
        this.convertManager = convertManager;
    }

    public void loadData(long sampleTime, List<Map<Integer, Object>> rows0){
        if (!rows0.isEmpty()) {
            this.storeManager.getDatabaseDAO().getMainDataDAO().putMainDataNoOverwrite(
                    new MainData(sampleTime, loadFromCollectionToArray(rows0))
            );
        }
    }

    private int [][] loadFromCollectionToArray(List<Map<Integer, Object>> rows){
        int numberOfCol = rows.stream().findFirst().get().size();
        int [][] out = new int[rows.size()][numberOfCol];

        AtomicInteger atomicInt = new AtomicInteger(0);
        rows.stream().forEach(e -> {

            int [] rowOut = new int[numberOfCol];

            try {
                for (int i = 0; i < e.entrySet().size(); i++) {
                    rowOut[i] = this.convertManager.convertFromRawToInt(sqlColMetadata.get(i), e.get(i));
                }
            } catch (Exception err) {
                log.error(err.getMessage());
                log.error(Arrays.toString(err.getStackTrace()));
            }

            out [atomicInt.getAndIncrement()] = rowOut;
        });

        return out;
    }

}
