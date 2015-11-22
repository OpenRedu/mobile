package br.com.developer.redu.api;

import java.util.List;


/**
 * @author igor
 *
 * @param <T> - Tipo do AVA
 */
public interface EnvironmentMethods<T> {
	
	/**
	 * Retorna a lista de AVAs que o usuário participa.
	 * 
	 * @return Uma lista de AVAs
	 */
	public List<T> getEnvironments();
	
    /**
     * Retorna o AVA especificado. Tanto o ID quanto o path podem ser utilizados aqui, 
     * porém o path é passível de modificações pelo usuário, 
     * portanto pode não servir como um identificador único.
     * 
    
     * @param environmentId - ID ou path do AVA
     * @return Um AVA
     */
    public T getEnvironment(String environmentId);

    
    /**
     * Cria AVA. Por si só o AVA é apenas uma maneira de organizar um 
     * conjunto de cursos dentro do contexto de uma instituição.
     * 
     * @param name - [OBRIGATÓRIO] Nome do ambiente. Máximo 40 caracteres; deve ser único
     * @param path - [OBRIGATÓRIO] Nome que aparecerá na barra de endereço.  
     * Deve respeitar a seguinte regex: ^[-_A-Za-z0-9]*$
     * @param initials - [OBRIGATÓRIO] Abreviação (usado em alguns lugares da interface). 
     * Deve ser único; Máximo de 10 caracteres
     * @param description - Descrição do ambiente. Máximo de 400 caracteres
     * @return O ambiente criado.
     */
    public T postEnvironment(String name, String path, String initials, String description);

    /**
     * Atualiza informações do AVA em questão.
     * 
     * @param name - Nome do ambiente. Máximo 40 caracteres; deve ser único
     * @param path - Nome que aparecerá na barra de endereço.  
     * Deve respeitar a seguinte regex: ^[-_A-Za-z0-9]*$
     * @param initials - Abreviação (usado em alguns lugares da interface). 
     * Deve ser único; Máximo de 10 caracteres
     * @param description - Descrição do ambiente. Máximo de 400 caracteres
     * @return O ambiente editado.
     */
    public void editEnvironment(String environmentId,String name, String path, String initials, String description);

    /**
     * Remove o AVA em questão. Todos os cursos, disciplinas, módulos e aulas 
     * insteridos neste ambiente serão perdidos e não estarão mais acessíveis aos usuários.
     * 
     * @param environmentId - ID ou path do AVA
     */
    public void deleteEnvironment(String environmentId);

}
