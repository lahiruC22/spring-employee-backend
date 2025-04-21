nix
{ pkgs, ... }: {
  channel = "stable-24.05";

  packages = [
    pkgs.zulu17
    pkgs.maven
  ];

  services.postgres = {
    enable = true;
  };

  env = {
    SPRING_PROFILES_ACTIVE = "dev";
    SPRING_DATASOURCE_URL = "jdbc:postgresql://ep-yellow-morning-a4gks8el-pooler.us-east-1.aws.neon.tech/employeedb?sslmode=require";
    SPRING_DATASOURCE_USERNAME = "employeedb_owner";
  };

  idx = {
    extensions = [
      #"vscjava.vscode-java-pack"
      #"rangav.vscode-thunder-client"
    ];

    previews = {
      enable = true;
      previews = {
        web = {
          command = [
            "./mvnw"
            "spring-boot:run"
            "-Dspring-boot.run.arguments=--server.port=$PORT"
          ];
          manager = "web";
          cwd = ".";
        };
      };
    };

    workspace = {
      onCreate = {
        install = "mvn clean install";
      };
    };
  };
}