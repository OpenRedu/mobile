package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo do Space
 */
public interface SpaceMethods<T> {

	/**
	 * Retorna a disciplina em questão.
	 * @param spaceId - ID da disciplina
	 * @return Uma disciplina
	 */
	public T getSpace(String spaceId);

    /**
     * Atualiza as informações da disciplina.
     * @param spaceId - [OBRIGATÓRIO] ID da disciplina
     * @param name - Nome do space. Máximo 40 caracteres; deve ser único
     * @param description - Descrição do space. Máximo de 400 caracteres
     */
    public void editSpace(String spaceId, String name, String description);

    /**
     * Cria uma disciplina no contexto do course especificado por course_id.
     * @param courseId - [OBRIGATÓRIO] ID do curso
     * @param name - [OBRIGATÓRIO] Nome do space. Máximo 40 caracteres; deve ser único
     * @param description - Descrição do space. Máximo de 400 caracteres
     */
    public T postSpace(String courseId, String name, String description);

    /**
     * Retorna uma lista com as disciplinas do curso especificado.
     * @param courseId - ID ou path do curso
     * @return Uma lista de disciplinas
     */
    public List<T> getSpacesByCourse(String courseId);

    
    /**
     * Remove uma disciplina. Os que fazem parte do curso ao qual a disciplina 
     * faz parte não terão mais acesso aos módulos e aulas.
     * 
     * @param spaceId - ID da disciplina
     */
    public void deleteSpace(String spaceId);
}
