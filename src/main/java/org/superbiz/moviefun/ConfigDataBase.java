package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class ConfigDataBase {

    @Bean(name = "albums-mysql")
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql"));
        return dataSource;
    }

    @Bean(name = "movies-mysql")
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql"));
        return dataSource;
    }

    /**
     * LocalContainerEntityManagerFactoryBean.
     */
    @Bean(name = "emf-album")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryAlbum(@Qualifier("albums-mysql") DataSource dataSource){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", "true");
        LocalContainerEntityManagerFactoryBean emfb =
                new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setPackagesToScan("org.superbiz.moviefun.albums");
        emfb.setJpaProperties(props);
        emfb.setJpaVendorAdapter(adapter);
        emfb.setPersistenceUnitName("album-unit");
        return emfb;
    }

    @Bean(name = "emf-movie")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryMovie(@Qualifier("movies-mysql") DataSource dataSource){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", "true");
        LocalContainerEntityManagerFactoryBean emfb =
                new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setPackagesToScan("org.superbiz.moviefun.movies");
        emfb.setJpaProperties(props);
        emfb.setJpaVendorAdapter(adapter);
        emfb.setPersistenceUnitName("movie-unit");
        return emfb;
    }

    @Bean(name = "album-unit")
    public EntityManager entityManagerAlbum(@Qualifier("emf-album") LocalContainerEntityManagerFactoryBean emfb){
        return emfb.getNativeEntityManagerFactory().createEntityManager();
    }

    @Bean(name = "movie-unit")
    public EntityManager entityManagerMovie(@Qualifier("emf-movie") LocalContainerEntityManagerFactoryBean emfb){
        return emfb.getNativeEntityManagerFactory().createEntityManager();
    }

    /**
     * JpaTransactionManager.
     */
//    @Bean(name = "txAlbum")
//    public PlatformTransactionManager transactionManagerAlbum(@Qualifier("album-unit") EntityManager em) {
//        JpaTransactionManager tm = new JpaTransactionManager();
//        tm.setEntityManagerFactory(em.getEntityManagerFactory());
//        return tm;
//    }
//    @Bean(name = "txMovie")
//    public PlatformTransactionManager transactionManagerMovie(@Qualifier("movie-unit") EntityManager em) {
//        JpaTransactionManager tm = new JpaTransactionManager();
//        tm.setEntityManagerFactory(em.getEntityManagerFactory());
//        return tm;
//    }

//    @Bean(name = "txAlbum")
//    public PlatformTransactionManager transactionManagerAlbum(@Qualifier("emf-album") EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
//    @Bean(name = "txMovie")
//    public PlatformTransactionManager transactionManagerMovie(@Qualifier("emf-movie") EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }

}
