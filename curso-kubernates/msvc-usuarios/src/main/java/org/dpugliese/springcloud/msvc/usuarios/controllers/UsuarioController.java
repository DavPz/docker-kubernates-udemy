package org.dpugliese.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.dpugliese.springcloud.msvc.usuarios.models.entity.Usuario;
import org.dpugliese.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.porId(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario.get());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(populateErrors(result));
        }

        if (!usuario.getEmail().isEmpty() && usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "El email ya existe"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario)); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(populateErrors(result));
        }
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuarioDb = usuarioOptional.get();

        if (!usuario.getEmail().isEmpty() &&
                usuarioService.findByEmail(usuario.getEmail()).isPresent() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "El email ya existe"));
        }

        usuarioDb.setNombre(usuario.getNombre());
        usuarioDb.setEmail(usuario.getEmail());
        usuarioDb.setPassword(usuario.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private HashMap<String, String> populateErrors(BindingResult result) {
        HashMap<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return errores;
    }

}
