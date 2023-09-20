package org.dpugliese.springcloud.msvc.usuarios.controllers;

import org.dpugliese.springcloud.msvc.usuarios.models.entity.Usuario;
import org.dpugliese.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Long id){
        Optional<Usuario> usuario = usuarioService.porId(id);
        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario.get());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario)); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario, @PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if(usuarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Usuario usuarioDb = usuarioOptional.get();
        usuarioDb.setNombre(usuario.getNombre());
        usuarioDb.setEmail(usuario.getEmail());
        usuarioDb.setPassword(usuario.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if(usuarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
