package com.my.container.spi.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * The service loader class. This an implementation of service provider. Look at
 * <a href="http://download.oracle.com/javase/1.5.0/docs/guide/jar/jar.html#Service Provider">http://download.oracle.com/javase/1.5.0/docs/guide/jar/jar.html#Service Provider</a>.
 * </p>
 * <br/>
 * <p>
 * <b>Service Provider configuration file</b>
 * <br/>
 * <br/>
 * A service provider identifies itself by placing a provider-configuration file in the resource directory META-INF/services.
 * The file's name should consist of the fully-qualified name of the abstract service class. The file should contain a
 * newline-separated list of unique concrete provider-class names. Space and tab characters, as well as blank lines, are ignored.
 * The comment character is '#' (0x23); on each line all characters following the first comment character are ignored.
 * The file must be encoded in UTF-8.
 * <br/>
 * <br/>
 * Example of configuration file content :
 * <pre>
 * com.my.MyProviderImplementation # My Provider implementation comment
 * <pre>
 * </p>
 *
 * @author kevinpollet
 */
public final class ServiceLoader<S> implements Iterable<S> {

    /**
     * Folder where service providers can
     * add their configuration file.
     */
    private static final String PREFIX = "META-INF/services/";

    /**
     * The comment character used in
     * service provider configuration file.
     */
    private static final String COMMENT_CHARACTER = "#";

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(ServiceLoader.class);

    /**
     * The service provider contract.
     */
    private Class<S> contract;

    /**
     * The service providers.
     */
    private List<S> providers;

    /**
     * The default constructor.
     */
    private ServiceLoader(final Class<S> contract) {
        this.contract = contract;
        this.providers = new ArrayList<S>();
        this.load();
    }

    /**
     * Load service provider. A service provider would have a file
     * a file with the name of the service implemented in {@code META-INF/services}
     * folder.
     *
     * @param contract the service provider contract
     * @param <S> the service provider contract type
     * @return the service loader
     */
    public static <S> ServiceLoader<S> load(final Class<S> contract) {
        return new ServiceLoader<S>(contract);
    }

    /**
     * Load the service provider implementations.
     */
    private void load() {

        this.logger.info("Load the service provider implementation of contract {}", contract.getName());

        try {

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> files = classLoader.getResources(PREFIX + contract.getName());

            while(files.hasMoreElements()) {
                URL file = files.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()));

                String line;
                while((line = reader.readLine()) != null) {

                    if (!line.startsWith(COMMENT_CHARACTER)) {

                        String className;
                        if (line.contains(COMMENT_CHARACTER)) {
                            className = line.split(COMMENT_CHARACTER)[0].trim();
                        } else {
                            className = line.trim();
                        }

                        // Load provider implementation
                        try {

                            this.logger.info("Load provider implementation {}", className);
                            Class<? extends S> clazz = (Class<? extends S>) Class.forName(className);
                            this.providers.add(contract.cast(clazz.newInstance()));

                        } catch (ClassNotFoundException ex) {
                            throw new ServiceLoaderException(String.format("The class %s is not in the class path", className), ex);
                        } catch (InstantiationException ex) {
                            throw new ServiceLoaderException(String.format("The class %s cannot be instantiated", className), ex);
                        } catch (IllegalAccessException ex) {
                            throw new ServiceLoaderException(String.format("The class %s cannot be instantiated because class constructor is private", className), ex);
                        }

                    }
                }

            }

        } catch (IOException ex) {
            this.logger.error("Error when reading service provider files of contract {}", contract.getName());
            throw new ServiceLoaderException("Error when reading service provider files", ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    public Iterator<S> iterator() {
        return providers.iterator();
    }

}
