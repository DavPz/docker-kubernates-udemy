package org.dpugliese.springcloud.msvc.cursos.repositories;

import org.dpugliese.springcloud.msvc.cursos.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
