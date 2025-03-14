# 쉽고 빠르게 끝내는 Pinpoint 설치 가이드

이 글에서는 APM(Application Performance Monitoring) 도구인 Pinpoint를 Docker 기반으로 쉽게 설치하는 방법을 다룬다.  
Spring Boot 애플리케이션에서 Pinpoint를 활용해 성능 모니터링을 적용하는 과정을 차근차근 살펴보자.

## 목차

1. [실행 환경](#실행-환경)
2. [manage 프로그램 설치 및 실행 (collector, web, hbase)](#manage-프로그램-설치-및-실행-collector-web-hbase)
3. [agent 설치 및 실행](#agent-설치-및-실행)
4. [마무리](#마무리)

## 실행 환경
Java **21**  
Spring Boot **3.3.5**  

설치할 프로그램
- pinpoint-agent : 애플리케이션에 설치하여 데이터를 수집
- pinpoint-collector : agent에서 보낸 데이터를 수집
- pinpoint-web : UI 대시보드 제공
- hbase : 성능 데이터를 저장

Pinpoint는 분산 환경에서 사용하는 것이 일반적이므로, 정확한 성능 측정을 위해 **pinpoint-agent는 실행할 애플리케이션과 동일한 장비에 설치**하고, **pinpoint-collector, pinpoint-web, hbase는 별도의 운영 장비에서 실행하는 것을 권장**한다.    

## manage 프로그램 설치 및 실행 (collector, web, hbase)

manage 프로그램을 설치할 서버에 접속하여 pinpoint-docker 프로젝트를 clone 한다. (https://github.com/pinpoint-apm/pinpoint-docker.git)

clone 하게 되면 docker-compose 파일이 포함되어있다. 이번엔 간단하게 collector, web, hbase만 실행할 예정이기 때문에 pinpoint-docker, redis, batch 등 나머지 설정은 제외한다.  

collector, web, hbase depends_on에 앞서 제외했던 컨테이너가 포함되어 있으므로 잘 제거해준다.  

docker-compose 명령어를 통해 manage 프로그램을 실행한다.

## agent 설치 및 실행

mangage 프로그램이 설치됐다면 Spring Boot 애플리케이션에 pinpoint-agent를 설치한다.  

### pinpoint-agent 다운로드

[pinpoint-apm/pinpoint/releases](https://github.com/pinpoint-apm/pinpoint/releases) 에서 pinpoint-agent 압축 파일을 내려받는다. 나는 `wget`명령어를 사용했다.  
```
wget https://github.com/pinpoint-apm/pinpoint/releases/download/3.0.1/pinpoint-agent-3.0.1.tar.gz
```
> 💡 agent 설치 시 주의할 점  
> - agent 설치 시 사용하는 Java 버전이 21 이상일 경우 agent 버전이 3.0 이상이어야함 
> - Java 버전에 맞는 pinpoint 버전은 [공식 문서]((https://github.com/pinpoint-apm/pinpoint?tab=readme-ov-file#compatibility))에서 확인 

### agent 설정

pinpoint-agent 압축을 해제하면 `pinpoint-root.config`파일을 확인할 수 있다.  

`pinpoint-root.config` 파일에 pinpoint-collector 서버의 IP를 입력한다. IP는 환경 변수로 설정하면 더 유연하게 관리할 수 있다.

### 애플리케이션과 pinpoint-agent 연결

Spring Boot 애플리케이션을 실행할 때 **agent 옵션**을 추가해야 한다.
> 💡 애플리케이션 실행 시 주의할 점
> - 애플리케이션 실행을 로컬 환경이 아닌 GitHub Actions 등 원격 서버를 통해 빌드하고 있다면 설치한 agent 파일이 원격 서버에서도 포함될 수 있도록 한다.

- javaagent
- pinpoint.agentId
- applicationName
- pinpoint.config
- profiler.transport.grpc.collector.ip

`jib`를 이용해 이미지를 빌드하고 있다면 다음 설정을 참고한다.

`jib.extraDirectories`를 이용해 agent 파일이 포함된 디렉토리를 이미지에 포함한다. extraDirectories는 `src/main/jib` 디렉토리 내의 모든 파일을 이미지의 target 디렉토리로 복사하여 동일한 구조를 유지한다.  

만약 `src/main/jib`가 아니라 다른 경로에 위치한 디렉토리를 이미지에 포함하고 싶다면 `jib.extraDirectories.paths`를 이용하면 된다. (관련 링크 : https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#adding-arbitrary-files-to-the-image)

실제로 적용해본 agent 설정은 다음과 같다. 

```groovy
jib {
	from {
		image = "eclipse-temurin:21-alpine"
	}
	extraDirectories {
		paths {
			path {
				from = file('src/main/jib/pinpoint-agent-3.0.1')
				into = '/app/libs/pinpoint-agent'
			}
		}
	}
	container {
		ports = ["8080"]
		jvmFlags = [
			"-Xms512m",
			"-Xmx1024m",
			"-javaagent:/app/libs/pinpoint-agent/pinpoint-bootstrap-3.0.1.jar",
			"-Dspring.profiles.active=${System.getenv('ACTIVE_PROFILE')}",
			"-Dprofiler.transport.grpc.collector.ip=${System.getenv('PINPOINT_COLLECTOR_IP')}",
			"-Dpinpoint.config=/app/libs/pinpoint-agent/pinpoint-root.config",
			"-Dpinpoint.agentId=${System.getenv('PINPOINT_AGENT_ID')}",
			"-Dpinpoint.applicationName=${System.getenv('PINPOINT_APP_NAME')}"
		]
	}
}
```

## 마무리

이번에 사이드 프로젝트를 하면서 트랜잭션을 포함한 더 자세한 성능 측정을 해보기 위해 pinpoint 를 설치해보았다.  
pinpoint를 비롯해 javaagent 설정도 처음이라 여러 에러를 보면서 시간이 꽤나 소모되었다. 알고보면 간단히 설치할 수 있기 때문에 이 글을 보고 pinpoint 설치에 조금이나마 도움이 되었으면 한다.  
