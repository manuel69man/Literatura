



package com.aluracursos.literatura.principal;

import com.aluracursos.literatura.models.*;
import com.aluracursos.literatura.repository.*;
import com.aluracursos.literatura.services.ConsumoAPI;
import com.aluracursos.literatura.services.ConvierteDatos;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final Logger log = LoggerFactory.getLogger(Principal.class);
    private final String menu = """
            ---Bienvenido a Consulta Libros, ELija la opción a través de un número----
            1- Buscar libro por título
            2- Listar libros registrados
            3- Listar autores registrados
            4- Listar autores vivos de un determinado año
            5- Listar libros por idiomas
            0- Salir
            """;
    @Autowired
    private Libro2Repository repository;
    @Autowired
    private Autor2Repository repositoryAutor;
    @Autowired
    private  IdiomasRepository idiomasRepository;

    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI= new ConsumoAPI();
    private Scanner teclado=new Scanner(System.in);
    private ConvierteDatos conversor = new ConvierteDatos();
    private DatosLibro datosDelLibro;
    private Set<DatosAutor> autorsNoRepetidos;
    private Set<String> idiomas;



    public Principal(Libro2Repository repository, Autor2Repository repositoryAutor,IdiomasRepository repositoryIdiomas) {
        this.repository = repository;
        this.repositoryAutor= repositoryAutor;
        this.idiomasRepository=repositoryIdiomas;

    }

    public void muestraElMenu()  {


        var opcion = -1;
        while (opcion != 0) {
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAno();
                    break;
                case 5:
                    listarLibrosPorIdiomas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    opcion=0;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void getDatosLibro() {
        System.out.println("Escribe el nombre del libro que desees registrar: ");
        var libro = teclado.nextLine();
        var hh = URL_BASE+"?search="+libro.replace(" ","+").toLowerCase();
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+libro.replace(" ","+").toLowerCase());
        DatosJson datosResult    =conversor.obtenerDatos(json, DatosJson.class);
        if ((datosResult.results()==null) || (datosResult.results().size()==0)) {
        } else {
            List<DatosLibro> librosPorTitulo = datosResult.results().stream()
                    .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase())).toList();
            if (librosPorTitulo.size()>=1) {
                Long descargas = datosResult.results().stream()
                        .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase()))
                        .     map(DatosLibro::numeroDeDescargas).reduce(0L,(ac, db)->ac+db);

                idiomas = datosResult.results().stream()
                        .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase()))
                        .flatMap(d->d.idiomas().stream()).distinct()
                        .collect(Collectors.toSet());

                List<DatosAutor> listAutores = datosResult.results().stream()
                        .filter(d -> d.titulo().toLowerCase().equals(libro.toLowerCase()))
                        .flatMap(d -> d.autores().stream()
                                .map(a -> new DatosAutor(a.nombre(), a.anoDeNacimiento(), a.anoDeMuerte())))
                        .toList();
                autorsNoRepetidos =listAutores.stream().collect(Collectors.toSet());
                String titulo = librosPorTitulo.stream().distinct().collect(Collectors.toList()).stream().findAny().get().titulo();
                this.datosDelLibro = new DatosLibro(titulo,null,descargas,null);
            }
        }
    }



    private void listarLibrosPorIdiomas() {
        System.out.println("Estos son idiomas disponibles que estan registrados");
        System.out.println(idiomasRepository.findAll());
        System.out.println("Ingrese el idioma del cual quiere saber que liubros han sido publucados: ");
        var idioma = teclado.nextLine();
            Optional<Idiomas> idiomaBuscado = idiomasRepository.findByIdioma(idioma);
            if (!idiomaBuscado.isEmpty()){
                System.out.println("Estos son los libros disponibles para el idioma "+idioma);
                List<String> libros=repository.findAllByLanguage(idiomaBuscado.get().getId());
                if (libros!=null){
                    libros.stream().forEach(System.out::println);
                }
            }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("introduce el año en que quieres consultar: ");
        var anoConsulta = teclado.nextLine();
        try{
            List<Autor2> autores=repositoryAutor.findAll();
            if (!autores.isEmpty()){
                autores.stream().filter(a->(a.getAnoDeMuerte()>Integer.valueOf(anoConsulta))&&(a.getAnoDeNacimiento()<Integer.valueOf(anoConsulta)))
                        .collect(Collectors.groupingBy(a->a.getnombre())).values().forEach(System.out::println);
            }
        } catch (NumberFormatException e){
            System.out.println("Intente un año valido!!");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor2> autoresReg = repositoryAutor.findAll();
        if (!autoresReg.isEmpty()) {
            autoresReg.forEach(System.out::println);
            System.out.println();
        } else System.out.println("La base de datos de Libros no contiene ningun Autor registrado, ¡DEBES REGISTRAR LIBROS PRIMERO!");

    }

    private void listarLibrosRegistrados() {
        List<Libro2> librosReg = repository.findAll();
        if (!librosReg.isEmpty()) {
            librosReg.forEach(System.out::println);
            System.out.println();
        } else System.out.println("La base de datos de Libros no contiene ningun libro registrado, ¡DEBES REGISTRAR LIBROS PRIMERO!");
    }

    @Transactional
    private void buscarLibroPorTitulo() {
        getDatosLibro();
        if (datosDelLibro !=null) {
            Set<Autor2> autoresPersistidos = new HashSet<>();
            for (DatosAutor datosAutor : autorsNoRepetidos) {
                try {
                    Autor2 autor = repositoryAutor.findByNombre(datosAutor.nombre())
                            .orElseGet(() -> repositoryAutor.save(new Autor2(datosAutor.nombre(), datosAutor.anoDeNacimiento(), datosAutor.anoDeMuerte())));
                    autoresPersistidos.add(autor);
                } catch (DataIntegrityViolationException e) {
                    System.out.println("Error: se intento grabar un autor existente" + e.getMessage());
                }
            }

            Set<Idiomas> idiomasPersistidos = new HashSet<>();
            for (String idioma : idiomas) {
                try {
                    Idiomas idiomaPersistido = idiomasRepository.findByIdioma(idioma)
                            .orElseGet(() -> idiomasRepository.save(new Idiomas(idioma)));
                    idiomasPersistidos.add(idiomaPersistido);
                } catch (DataIntegrityViolationException e) {
                    System.out.println("Error: se intento grabar un autor existente" + e.getMessage());
                }
            }
            Libro2 auxLibro = new Libro2();
            Optional<Libro2> libroExistente =repository.findByTitulo(datosDelLibro.titulo());
            if (libroExistente.isEmpty()) {
                Libro2 libro = new Libro2();
                libro.setTitulo(datosDelLibro.titulo());
                libro.setNumeroDeDescargas(datosDelLibro.numeroDeDescargas());
                libro.setAutores(autoresPersistidos);
                libro.setIdiomas(idiomasPersistidos);
                auxLibro=libro;
                repository.save(libro);
            }
            if (!libroExistente.isEmpty()) {
                System.out.println(libroExistente);}
            else {
                System.out.println(auxLibro);
            }
        } else {
            System.out.println("Libro no encontrado en la API de Gutendex, intentalo de nuevo!!");
        }
    }



}

