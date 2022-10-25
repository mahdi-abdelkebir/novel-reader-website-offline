package pj.service.abstr;
//package pj.service;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.HibernateException;
//import org.hibernate.event.spi.LoadEvent;
//import org.hibernate.event.spi.LoadEventListener;
//
//import com.boraji.tutorial.hibernate.entity.Book;
//import com.google.common.base.Optional;
//
//import pj.models.Novel;
//import pj.models.Reading;
//
//public class LoadEventListenerImp implements LoadEventListener {
//   
//   private static final long serialVersionUID = 1L;
//   private static Logger logger = LogManager.getLogger(LoadEventListenerImp.class);
//
//   @Override
//   public void onLoad(LoadEvent e, LoadType type) throws HibernateException {
//      logger.info("onLoad is called.");
//      Object obj = e.getResult();
//      if (obj instanceof Novel) {
//         Novel novel = (Novel) obj;
//
//         // populate using another repo bean
//         
// 		Optional<Reading> optional = readingRepository.getNovel(user_id, novel.getId());
//        if(!optional.isEmpty()) {
//        	novel.setAdded(true);
//        } else {
//        	novel.setAdded(false);
//        }
//      }
//   }
//
//}