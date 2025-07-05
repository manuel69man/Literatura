package com.aluracursos.literatura.principal;

import com.aluracursos.literatura.models.*;
import com.aluracursos.literatura.repository.LibroRepository;
import com.aluracursos.literatura.services.ConsumoAPI;
import com.aluracursos.literatura.services.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final String menu = """
            ---Bienvenido a Consulta Libros, ELija la opción a través de un número----
            1- Buscar libro por título
            2- Listar libros registrados
            3- Listar autores registrados
            4- Listar autores vivos de un determinado año
            5- Listar libros por idiomas
            0- Salir
            """;
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI= new ConsumoAPI();
    private Scanner teclado=new Scanner(System.in);
    private LibroRepository repository;
    private ConvierteDatos conversor = new ConvierteDatos();


    public Principal(LibroRepository repository) {
        this.repository = repository;
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
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private Libro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que desees registrar: ");
         var libro = teclado.nextLine();
        var hh = URL_BASE+"?search="+libro.replace(" ","+").toLowerCase();
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search="+libro.replace(" ","+").toLowerCase());

        DatosJson datosResult    =conversor.obtenerDatos(json, DatosJson.class);

        if ((datosResult.results()==null) || (datosResult.results().size()==0)) {

            return null;
        } else {

            List<DatosLibro> librosPorTitulo = datosResult.results().stream()
                    .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase())).toList();
            if (librosPorTitulo.size()>=1) {
                Long descargas = datosResult.results().stream()
                        .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase()))
                        .     map(DatosLibro::numeroDeDescargas).reduce(0L,(ac, db)->ac+db);




                List<Idiomas> listIdiomas = datosResult.results().stream()
                        .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase()))
                        .flatMap(d->d.idiomas().stream())
                        .map(d->new Idiomas(d))
                        .toList();

                List<Autor> listAutores =datosResult.results().stream()
                        .filter(d->d.titulo().toLowerCase().equals(libro.toLowerCase()))
                        .flatMap(d->d.autores().stream()
                                .map(a->new Autor(a.nombre(),a.anoDeNacimiento(),a.anoDeMuerte())))
                        .toList();
                List<Autor> autorsNoRepetidos =listAutores.stream().findAny().stream().toList();
                List<Idiomas> idiomasNoRepetidos= listIdiomas.stream().findAny().stream().toList();

                int  ggg = datosResult.results().size();

                String titulo = librosPorTitulo.stream().distinct().collect(Collectors.toList()).stream().findAny().get().titulo();

                Libro datosDelLibro = new Libro(titulo,descargas);
                datosDelLibro.setAutores(autorsNoRepetidos);
                datosDelLibro.setidiomas(idiomasNoRepetidos);

                return datosDelLibro;
            }


        } return null; /*
                SE RETORNA NULL PUES SE CONSIGUE UN TITULO QUE CONTIENE COMO PARTE DE EL EL TITULO QUE EL USUARIO INTRODUJO
                PERO QUE NO ES EXACTAMENTE EL TITULO DEL LIBRO, POR LO QUE SE PRESTA PARA AMBIGUEDAD
                        */
    }

    private void listarLibrosPorIdiomas() {
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var libro = teclado.nextLine();

    }

    private void listarAutoresVivosPorAno() {
    }

    private void listarAutoresRegistrados() {
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = repository.findAll();
        if (!libros.isEmpty()) {
            libros.forEach(System.out::println);
        } else System.out.println("La base de datos de Libros no contiene ningun libro registrado, ¡DEBES REGISTRAR LIBROS PRIMERO!");
    }

    private void buscarLibroPorTitulo() {
        Libro datosLibro=  getDatosLibro();
        if (datosLibro!=null){
            System.out.println(datosLibro);
            try {
                repository.save(datosLibro);
                System.out.println(datosLibro);
            } catch (DataIntegrityViolationException e){
                System.out.println("Error: se intento grabar unlibro existente");
            }

        } else {
            System.out.println("Libro no encontrado, intentalo de nuevo!!");
        }
    }

}
