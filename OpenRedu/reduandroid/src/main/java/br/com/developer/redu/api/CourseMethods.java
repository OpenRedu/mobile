package br.com.developer.redu.api;

import java.util.List;


/**
 * @author igor
 *
 * @param <T> - Tipo do curso
 */
public interface CourseMethods<T> {
	
    /**
     * Retorna o curso em questão.
     * 
     * @param courseId - ID ou path do curso
     * @return Um curso
     */
    public T getCourse(String courseId);
    
    
    /**
     * Retorna uma lista de cursos criados dentro do AVA em questão.
     * 
     * @param environmentId - ID ou path do AVA
     * @return Uma lista de cursos
     */
    public List<T> getCoursesByEnvironment(String environmentId);

    /**
     * Cria um novo curso no AVA especificado. No Redu a matrícula é realizada no curso. 
     * Ou seja, os usuários que possuem acesso a um curso 
     * também podem visualizar suas disciplinas, módulos e aulas.
     * 
     * @param environmentId - [OBRIGATÓRIO] ID ou path do AVA
     * @param name - [OBRIGATÓRIO] nome do curso. Máximo 40 caracteres. Deve ser único
     * @param path - [OBRIGATÓRIO] Nome que aparecerá na barra de endereço.
     * Deve respeitar a seguinte regex: ^[-_A-Za-z0-9]*$; Deve ser único dentro de um AVA 
     * @param workload - [OPCIONAL] Carga horária do curso em horas. Deve ser numérico
     * @param description - [OPCIONAL] Descrição do curso. Máximo de 400 caracteres
     * @return O curso criado.
     */
    public T postCourse(String environmentId, String name, String path, String workload, String description);;
    
    /**
     * Atualiza as informações do curso especificado. É importante perceber que não há 
     * proteção para a atualização do path. Ou seja, alguma aplicação pode acessar 
     * este recurso utilizando o path e, caso o mesmo seja modificado, 
     * o códido HTTP 404 (not found) será lançado.
     * 
     * @param courseId - ID ou path do curso
     * @param name - Nome do curso. Máximo 40 caracteres; deve ser único.
     * @param path - Nome que aparecerá na barra de endereço. 
     * Deve respeitar a seguinte regex: ^[-_A-Za-z0-9]*$; Deve ser único dentro de um AVA
     * @param workload - Carga horária do curso em horas. Deve ser numérico
     * @param description - Descrição do curso. Máximo de 400 caracteres
     */
    public void editCourse(String courseId, String name, String path, String workload, String description);

    /**
     * Remove o curso em questão. Todos os usuários serão desmatriculados e perderão o 
     * acesso ao recurso.
     * 
     * @param courseId - ID ou path do curso
     */
    public void deleteCourse(String courseId);

}
