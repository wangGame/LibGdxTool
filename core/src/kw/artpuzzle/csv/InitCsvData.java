package kw.artpuzzle.csv;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.kw.gdx.resource.csvanddata.demo.CsvUtils;

public class InitCsvData {
   public static ArrayMap<Integer,LevelOrder> orderHashMap;
   public static ArrayMap<Integer,LevelOrder> preOrderHashMap;

   public InitCsvData(){
      {
         orderHashMap = new ArrayMap<>();
         Array<LevelOrder> common = CsvUtils.common("csv/levelOrder.csv", LevelOrder.class);
         for (LevelOrder levelOrder : common) {
            orderHashMap.put(levelOrder.getGame_sort(), levelOrder);
//            orderHashMap.put(levelOrder.getLevel_num(), levelOrder);
         }
      }
      {
         preOrderHashMap = new ArrayMap<>();
         Array<LevelOrder> common = CsvUtils.common("csv/levelPreOrder.csv", LevelOrder.class);
         for (LevelOrder levelOrder : common) {
            preOrderHashMap.put(levelOrder.getGame_sort(), levelOrder);
         }
      }
   }

   public static void dispose(){
      orderHashMap = null;
      preOrderHashMap = null;
   }
}
