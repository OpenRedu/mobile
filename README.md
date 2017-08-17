![alt tag](https://raw.githubusercontent.com/ThaisaMirely/mobile/master/images-for-readme/banner_pt.png)

Mas afinal, o que é OpenRedu?

O Openredu[[1]](http://openredu.cin.ufpe.br) é um ambiente de aprendizagem opensource[[2]](http://softwarelivre.org/profile/openredu) ,
idealizado para proporcionar formas de colaboração entre estudantes e professores utilizando diferente mídias e tipos de interação virtual.
É uma plataforma em nuvem, seguindo as tendências do casamento entre tecnologia da informação e educação e favorecendo a realização de MOOCs[[3]](https://pt.wikipedia.org/w/index.php?title=MOOC&oldid=42547108) ,
como é feito, por exemplo, nas plataformas edX[[4]](https://en.wikipedia.org/w/index.php?title=EdX&oldid=669996788) e Coursera[[5]](https://pt.wikipedia.org/w/index.php?title=Coursera&oldid=42312876).

:earth_americas:  http://openredu.cin.ufpe.br
:octocat: https://github.com/OpenRedu

O OpenRedu móvel nasceu do [Redu móvel](https://github.com/redu/redu).
Herdamos o código fonte e toda a estrutura de guidelines e documentação.
Aos pouco estamos formando a cara e identidade do OpenRedu, remodelando seu código e atualizando práticas, dependências e funcionalidades.

:checkered_flag: Nossa versão atual ainda não está em loja :sob: (nos ajude contribuindo com a nossa comunidade :raised_hands:),
mas você pode checar a versão antiga [Redu Móvel](https://play.google.com/store/apps/details?id=br.com.redumobile&hl=pt_BR)(código base da nossa aplicação).

# Começando

## Requisitos

* [Android Studio](https://developer.android.com/sdk/index.html)
* [Android SDK Tools > 18](https://developer.android.com/sdk/installing/index.html)
* [Gradle >= 1.3.0 ](http://gradle.org/gradle-download/)

## Android
O OpenRedu móvel é desenvolvido em Java utilizando a plataforma Android.
O projeto foi desenvolvido utilizando a IDE Eclipse e posteriormente migrado para a IDE Android Studio.
Todas as dependências que anteriormente eram libs externas, agora se encontram como módulos do projeto.
Novas dependências(bibliotecas), devem ser adicionada via Maven através do Gradle. Nossa ideia é desacoplarmos todas as libs em formato **.jar**

## Estrutura do Projeto

Exemplo:

![alt tag](https://raw.githubusercontent.com/ThaisaMirely/mobile/master/images-for-readme/project_structure.png)

### Módulos
 ```
 ':app' = core da aplicação. Projeto principal

 ':openReduMobileAndroidPushRefresh' = módulo(lib) para push refresh

 ':openReduMobileViewPagerIndicator' = módulo(lib) para paginação

 ':openReduREST'= módulo(lib) Android REST

 ':openReduMobileWebCachedImageView' = módulo(lib) para tratamento de web cache de imagem.
  ```
### Build Automática

 O processo de build automática é realizado por meio da ferramenta [Gradle](http://gradle.org).
 Atualmente é utilizada a versão 1.3.0, nativa do Android Studio.        

 ` classpath 'com.android.tools.build:gradle:1.3.0' `

 Para cada módulo do projeto há um arquivo **build.gradle** com as dependências e configurações do módulo.

 [Aqui](http://developer.android.com/intl/pt-br/tools/building/configuring-gradle.html) mais informações sobre como utilizar o Gradle no Android Studio.

 Exemplo - app módulo:

 ```
 apply plugin: 'com.android.application'

android {
    compileSdkVersion 19
    buildToolsVersion '19.1.0'

    defaultConfig {
        applicationId "br.ufpe.cin.openredu"
        minSdkVersion 10
        targetSdkVersion 19
        versionCode 1
        versionName "1.0"
    }
   ...
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:19.1.0'
    compile 'com.android.support:support-v4:19.1.0'
    compile files('libs/buzzboxSDK-0.6.5.jar')
    compile project(':openReduREST')
    compile project(':openReduMobileAndroidPushRefresh')
    compile project(':openReduMobileViewPagerIndicator')
    compile project(':openReduMobileWebCachedImageView')
}

```

## Contribuir com o projeto

### Configurações

* faça o download(![alt tag](https://raw.githubusercontent.com/ThaisaMirely/mobile/master/images-for-readme/icon_donwload.png)) do projeto  ou realize o clone via ![alt tag](https://raw.githubusercontent.com/ThaisaMirely/mobile/master/images-for-readme/icon_clone_projet.png)    

com seu ambiente configurado(Android Studio + SDK + Gradle):

* Importe no Android Studio o projeto ***mobile/OpenRedu***.
Exemplo:

![alt tag](https://raw.githubusercontent.com/ThaisaMirely/mobile/master/images-for-readme/import_project.png)

* Compile e execute o projeto para testar e iniciar o desenvolvimento.
* Caso não tenha o [plugin do Findbugs](https://plugins.jetbrains.com/plugin/3847?pr=idea) instalado, deverá realizar esse passo.

### Pull requests

Os passos para contribuir com a evolução do código, seja para resolução de issue ou criação de features são os seguintes:

- Criar um branch novo
- Realizar mudanças ou adicionar a feature
- Commitar mudanças e enviá-las para o remoto
- Realizar **pull request**


### Qualidade de Código

Integrado ao Android Studio há um ótima ferramenta de análise estática chama [Lint](http://developer.android.com/intl/pt-br/tools/help/lint.html).

Utilizamos ela para gerarmos nossos relatórios de ***saúde do código***.

Junto com o Lint, também utilizamos o [Findbugs](http://findbugs.sourceforge.net), que é mais voltado para a análise estática com foco em padrão de desenvolvimento Java, visto que o Lint é direcionado para especificações do projeto Android.

Para utilizar junto com seu ambiente Android Studio, é necessário que seja instalado um plugin.

[Aqui](https://androidbycode.wordpress.com/2015/02/13/static-code-analysis-automation-using-findbugs-android-studio/) há um tutorial que pode lhe ajudar

:checkered_flag: Sempre que antes de realizar um ***pull request*** , cheque(executando as ferramentas estáticas) se a versão mais atual do relatório está igual a que foi gerada por você.
Caso não esteja(o número tenha aumentado), corrija antes de submeter sua contribuição.

### Reportando issues

:construction: Estamos criando um template para abertura de bugs, melhorias e novas funcionalidade.

### Estilo de Código

Estamos construindo nosso ***Guia de estilo***. :construction::grin:
Enquanto ele não está pronto, você pode seguir os padrões abaixo para garantir a qualidade do seu código.

- (https://source.android.com/source/code-style.html)
- (https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md)

:checkered_flag: Leia com atenção antes de submeter seu pull request :wink:.

## Comunidade

O OpenRedu está presente nos seguintes sítios:

- [Fórum](http://forum.openredu.com/)
- [Perfil Software Livre Brasil](http://softwarelivre.org/profile/openredu)
- [Facebook](https://www.facebook.com/redesocialeducacional/)
- [Youtube](https://www.youtube.com/user/openredu)
- [Wikipédia](https://pt.wikipedia.org/wiki/Openredu)

## Licença
 ```
                GNU GENERAL PUBLIC LICENSE
                    Version 2, June 1991
Copyright (C) 1989, 1991 Free Software Foundation, Inc., <http://fsf.org/>
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
Everyone is permitted to copy and distribute verbatim copies
of this license document, but changing it is not allowed.
 ```

 :checkered_flag: Para informação completa acesse o arquivo LICENSE, na raiz do projeto.
