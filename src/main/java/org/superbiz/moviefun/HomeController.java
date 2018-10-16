package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import javax.persistence.EntityManager;
import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final TransactionTemplate transactionTemplateAlbum;
    private final TransactionTemplate transactionTemplateMovie;

//    PlatformTransactionManager txMngAlbum;
//    PlatformTransactionManager txMngMovie;

    @Autowired
    public HomeController(
            MoviesBean moviesBean,
            AlbumsBean albumsBean,
            MovieFixtures movieFixtures,
            AlbumFixtures albumFixtures,
//            @Qualifier("txAlbum") PlatformTransactionManager txMngAlbum,
//            @Qualifier("txMovie") PlatformTransactionManager txMngMovie)
            @Qualifier("album-unit") EntityManager emAlbum,
            @Qualifier("movie-unit") EntityManager emMovie)
    {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
//        this.txMngAlbum = txMngAlbum;
//        this.txMngMovie = txMngMovie;
//        this.transactionTemplateAlbum = new TransactionTemplate(txMngAlbum);
//        this.transactionTemplateMovie = new TransactionTemplate(txMngMovie);
        this.transactionTemplateAlbum = new TransactionTemplate(transactionManager(emAlbum));
        this.transactionTemplateMovie = new TransactionTemplate(transactionManager(emMovie));
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {

        transactionTemplateAlbum.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus transactionStatus) {
                for (Album album : albumFixtures.load()) {
                    albumsBean.addAlbum(album);
                }
                return null;
            }
        });

        transactionTemplateMovie.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus transactionStatus) {
                for (Movie movie : movieFixtures.load()) {
                    moviesBean.addMovie(movie);
                }
                return null;
            }
        });

//        for (Movie movie : movieFixtures.load()) {
//            moviesBean.addMovie(movie);
//        }

//        for (Album album : albumFixtures.load()) {
//            albumsBean.addAlbum(album);
//        }

        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }

    public PlatformTransactionManager transactionManager(EntityManager em) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(em.getEntityManagerFactory());
        return tm;
    }
}
