package org.dpugliese.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.dpugliese.springcloud.msvc.cursos.models.Usuario;
import org.dpugliese.springcloud.msvc.cursos.models.entity.Curso;
import org.dpugliese.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.porId(id);
        if (curso.isPresent()) {
            return ResponseEntity.ok().body(curso.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(popupaleErrors(result));
        }

        Curso cursoDb = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(popupaleErrors(result));
        }
        Optional<Curso> cursoDb = cursoService.porId(id);
        if (cursoDb.isPresent()) {
            Curso cursoEditado = cursoDb.get();
            cursoEditado.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoEditado));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> cursoDb = cursoService.porId(id);
        if (cursoDb.isPresent()) {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioDb;
        try {
            usuarioDb = cursoService.asignarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(Collections.singletonMap("mensaje", "No existe el usuario o error en el servicio: " + e.getMessage()));
        }
        if (usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDb.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioDb;
        try {
            usuarioDb = cursoService.crearUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(Collections.singletonMap("mensaje", "No pudo crear el usuario o error en el servicio: " + e.getMessage()));
        }
        if (usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDb.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioDb;
        try {
            usuarioDb = cursoService.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status())
                    .body(Collections.singletonMap("mensaje", "No existe el usuario o error en el servicio: " + e.getMessage()));
        }
        if (usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDb.get());
        }
        return ResponseEntity.notFound().build();
    }


    private HashMap<String, String> popupaleErrors(BindingResult result) {
        HashMap<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return errors;
    }


}
