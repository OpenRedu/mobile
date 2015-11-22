package br.com.developer.redu.api;

import java.util.List;

/**
 * @author igor
 *
 * @param <T> - Tipo do Subject
 */
public interface SubjectMethods<T> {
	
    /**
     * Retorna o módulo em questão.
     * @param subjectId - ID do módulo
     * @return Um módulo
     */
    public T getSubject(String subjectId);

    /**
     * Retorna os módulos de uma disciplina.
     * @param spaceId - ID do módulo
     * @return Uma lista de módulos
     */
    public List<T> getSubjectsBySpace(String spaceId);

    /**
     * Cria um módulo no contexto da disciplina especificada por spaceId.
     * @param spaceId - [OBRIGATÓRIO] ID da disciplina.
     * @param title - [OBRIGATÓRIO] Nome do módulo. Máximo 40 caracteres.
     * @param description - Descrição do módulo. Máximo 250 caracteres
     * @return O módulo criado.
     */
    public T postSubject(String spaceId, String title, String description);

    /**
     * Atualiza as informações do módulo.
     * @param subjectId - ID da disciplina.
     * @param title - Nome do módulo. Máximo 40 caracteres.
     * @param description - Descrição do módulo. Máximo 250 caracteres
     * @return O módulo editado.
     */
    public void editSubject(String subjectId, String title, String description);

    /**
     * Remove um módulo. Os que fazem parte do módulo não terão mais acesso às aulas.
     * @param subjectId - ID do módulo
     */
    public void deleteSubject(String subjectId);

}


