package com.people.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	//200 Success
	  OK(200, "000000", "OK")
	//신규건 등록시 201
	, CREATED_OK(201, "201000", "Created OK")
    
    /**
     * Custom Exception 정의는 400번 대 Http Status Code를 사용한다.
     * 
     * 400 Bad Request
     * 	   이 응답은 잘못된 문법으로 인하여 서버가 요청하여 이해할 수 없음을 의미합니다.
     * 401 Unauthorized
     * 	   비록 HTTP 표준에서는 '미승인(unauthorized)'를 명확히 하고 있지만, 의미상 이 응답은 '비인증(unauthenticated)'를 의미합니다. 
     * 	   클라이언트는 요청한 응답을 받기 위해서는 반드시 스스로를 인증해야 합니다.
     * 402 Payment Required
     * 	   이 응답 코드는 나중에 사용될 것을 대비해 예약되었습니다. 첫 목표로는 디지털 결제 시스템에 사용하기 위하여 만들어졌지만 지금 사용되고 있지는 않습니다.
     * 403 Forbidden
     * 	   클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않습니다. 
     * 	   예를 들어, 그들은 미승인이어서 서버는 거절을 위한 적절한 응답을 보냅니다. 
     * 	   401과 다른 점은 서버가 클라이언트가 누구인지 알고 있습니다.
     * 404 Not Found
     * 	   서버는 요청받은 리소스를 찾을 수 없습니다. 브라우저에서는 알려지지 않은 URL을 의미합니다. 
     * 	   이것은 API에서 종점은 적절하지만 리소스 자체는 존재하지 않음을 의미할 수 있습니다. 
     * 	   서버들은 인증받지 않은 클라이언트로부터 리소스를 숨기기 위하여 이 응답을 403 대신에 전송할 수도 있습니다. 
     * 	   이 응답 코드는 웹에서 반복적으로 발생하기 때문에 가장 유명할지도 모릅니다.
     * 405 Method Not Allowed
     * 	   요청한 메소드는 서버에서 알고 있지만, 제거되었고 사용할 수 없습니다. 
     * 	   예를 들어, 어떤 API에서 리소스를 삭제하는 것을 금지할 수 있습니다.
     *                필수적인 메소드인 GET과 HEAD는 제거될 수 없으며, 이 에러 코드를 리턴할 수 없습니다.
     * 406 Not Acceptable
     * 	   이 응답은 서버가 서버 주도 콘텐츠 협상을 수행한 후, 사용자 에이전트에서 정해준 규격에 따른 어떠한 콘텐츠도 찾지 않았을 때, 웹서버가 보냅니다.
     * 407 Proxy Authentication Required
     * 	   이것은 401과 비슷하지만 프록시에 의해 완료된 인증이 필요합니다.
     * 408 Request Timeout
     * 	   이 응답은 요청을 한 지 시간이 오래된 연결에 일부 서버가 전송하며, 
     * 	   어떤 때에는 이전에 클라이언트로부터 어떠한 요청이 없었다고 하더라도 보내지기도 합니다. 
     * 	   이것은 서버가 사용되지 않는 연결을 끊고 싶어하는 것을 의미합니다. 
     * 	   이 응답은 특정 몇몇 브라우저에서 빈번하게 보이는데 Chrome, Firefox 27+, 또는 IE 9와 같은 웹서핑 속도를 올리기 위해 
     * 	   HTTP 사전 연결 메카니즘을 사용하는 브라우저들이 해당됩니다. 또한 일부 서버는 이 메시지를 보내지 않고 연결을 끊어버리기도 합니다.
     * 409 Conflict
     * 	   이 응답은 요청이 현재 서버의 상태와 충돌될 때 보냅니다.
     * 410 Gone
     * 	   이 응답은 요청한 콘텐츠가 서버에서 영구적으로 삭제되었으며, 전달해 줄 수 있는 주소 역시 존재하지 않을 때 보냅니다. 
     * 	   클라이언트가 그들의 캐시와 리소스에 대한 링크를 지우기를 기대합니다. 
     * 	   HTTP 기술 사양은 이 상태 코드가 '일시적인, 홍보용 서비스'에 사용되기를 기대합니다.
     * 	   API는 알려진 리소스가 이 상태 코드와 함께 삭제되었다고 강요해서는 안된다.
     * 411 Length Required
     * 	   서버에서 필요로 하는 Content-Length 헤더 필드가 정의되지 않은 요청이 들어왔기 때문에 서버가 요청을 거절합니다.
     * 412 Precondition Failed
     * 	   클라이언트의 헤더에 있는 전제조건은 서버의 전제조건에 적절하지 않습니다.
     * 413 Payload Too Large
     * 	   요청 엔티티는 서버에서 정의한 한계보다 큽니다. 서버는 연결을 끊거나 혹은 Retry-After 헤더 필드로 돌려보낼 것이다.
     * 414 URI Too Long
     * 	   클라이언트가 요청한 URI는 서버에서 처리하지 않기로 한 길이보다 깁니다.
     * 415 Unsupported Media Type
     * 	   요청한 미디어 포맷은 서버에서 지원하지 않습니다. 서버는 해당 요청을 거절할 것입니다.
     * 416 Requested Range Not Satisfiable
     * 	   Range 헤더 필드에 요청한 지정 범위를 만족시킬 수 없습니다. 범위가 타겟 URI 데이터의 크기를 벗어났을 가능성이 있습니다.
     * 417 Expectation Failed
     * 	   이 응답 코드는 Expect 요청 헤더 필드로 요청한 예상이 서버에서는 적당하지 않음을 알려줍니다.
     * 421 Misdirected Request
     * 	   서버로 유도된 요청은 응답을 생성할 수 없습니다. 이것은 서버에서 요청 URI와 연결된 스킴과 권한을 구성하여 응답을 생성할 수 없을 때 보내집니다.
     * 422 Unprocessable Entity (WebDAV)
     * 	   요청은 잘 만들어졌지만, 문법 오류로 인하여 따를 수 없습니다.
     * 423 Locked (WebDAV)
     * 	   리소스는 접근하는 것이 잠겨있습니다.
     * 424 Failed Dependency (WebDAV)
     * 	   이전 요청이 실패하였기 때문에 지금의 요청도 실패하였습니다.
     * 426 Upgrade Required
     * 	   서버는 지금의 프로토콜을 사용하여 요청을 처리하는 것을 거절하였지만, 
     * 	   클라이언트가 다른 프로토콜로 업그레이드를 하면 처리를 할지도 모릅니다. 
     * 	   서버는 Upgrade 헤더와 필요로 하는 프로토콜을 알려주기 위해 426 응답에 보냅니다.
     * 428 Precondition Required
     * 	   오리진 서버는 요청이 조건적이어야 합니다. 클라이언트가 리소스를 GET해서, 수정하고, 그리고 PUT으로 서버에 돌려놓는 동안 서드파티가 
     * 	   서버의 상태를 수정하여 발생하는 충돌인 '업데이트 상실'을 예방하기 위한 목적입니다.
     * 429 Too Many Requests
     * 	   사용자가 지정된 시간에 너무 많은 요청을 보냈습니다("rate limiting").
     * 431 Request Header Fields Too Large
     * 	   요청한 헤더 필드가 너무 크기 때문에 서버는 요청을 처리하지 않을 것입니다. 요청은 크기를 줄인 다음에 다시 전송해야 합니다.
     * 451 Unavailable For Legal Reasons
     * 	   사용자가 요청한 것은 정부에 의해 검열된 웹페이지와 같은 불법적인 리소스입니다.
     */
    
    //400 Bad Request
    //    이 응답은 잘못된 문법으로 인하여 서버가 요청하여 이해할 수 없음을 의미합니다.
    , BAD_REQUEST(400, "400000", "Bad Request")
    , PARAMETER_NOT_FOUND(400, "400001", "파라미터가 없거나 잘못되었습니다.")
    , WRONG_PARAMETER_VALUE(400, "400002", "파라미터 값을 확인해주세요.")

    // 404 Not Found
    // 	   서버는 요청받은 리소스를 찾을 수 없습니다. 브라우저에서는 알려지지 않은 URL을 의미합니다. 
    // 	   이것은 API에서 종점은 적절하지만 리소스 자체는 존재하지 않음을 의미할 수 있습니다. 
    // 	   서버들은 인증받지 않은 클라이언트로부터 리소스를 숨기기 위하여 이 응답을 403 대신에 전송할 수도 있습니다. 
    //	   이 응답 코드는 웹에서 반복적으로 발생하기 때문에 가장 유명할지도 모릅니다.
    , NOT_FOUND(404, "404000", "Not Found")
    , USERID_NOT_FOUND(404, "404001", "User ID 찾을 수 없습니다.")
    , EMAIL_NOT_FOUND(404, "404002", "Email 계정을 찾을 수 없습니다.")
    , ITEMS_NOT_FOUND(404, "404003", "요청하신 상품을 찾을 수 없습니다.")
    , BROWSER_NOT_FOUND(404, "404004", "Not supported client-browser ")

    //409 CONFLICT 중복된 리소스
    //    이 응답은 요청이 현재 서버의 상태와 충돌될 때 보냅니다.
    , CONFLICT_REQUEST(409, "409000", "REQUEST CONFLICT")
    , ALREADY_REGISTER_USERID(409, "409001", "이미 가입된 사용자 ID 입니다.")
    , ALREADY_SAVED_ORDER(409, "409002", "이미 처리된 주문입니다.")

    
    /**
     * 500 Internal Server Error
     * 	   웹 사이트 서버에 문제가 있음을 의미하지만 서버는 정확한 문제에 대해 더 구체적으로 설명할 수 없습니다.
     * 501 Not Implemented
     * 	   서버가 요청을 이행하는 데 필요한 기능을 지원하지 않음을 나타냅니다.
     * 502 Bad Gateway
     * 	   서버가 게이트웨이로부터 잘못된 응답을 수신했음을 의미합니다. 인터넷상의 서버가 다른 서버로부터 유효하지 않은 응답을 받은 경우 발생합니다.
     * 503 Service Unavailable
     * 	   서버가 요청을 처리할 준비가 되지 않았습니다. 일반적인 원인은 유지보수를 위해 작동이 중단되거나 과부하가 걸린 서버입니다. 이 응답과 함께 문제를 설명하는 사용자 친화적인 페이지가 전송되어야 한다는 점에 유의하십시오. 이 응답은 임시 조건에 사용되어야 하며, Retry-After: HTTP 헤더는 가능하면 서비스를 복구하기 전 예상 시간을 포함해야 합니다. 웹마스터는 또한 이러한 일시적인 조건 응답을 캐시하지 않아야 하므로 이 응답과 함께 전송되는 캐싱 관련 헤더에 대해서도 주의해야 합니다.
     * 504 Gateway Timeout
     * 	   웹페이지를 로드하거나 브라우저에서 다른 요청을 채우려는 동안 한 서버가 액세스하고 있는 다른 서버에서 적시에 응답을 받지 못했음을 의미합니다. 이 오류 응답은 서버가 게이트웨이 역할을 하고 있으며 적시에 응답을 받을 수 없을 경우 주어집니다. 이 오류는 대게 인터넷상의 서버 간의 네트워크 오류이거나 실제 서버의 문제입니다. 컴퓨터, 장치 또는 인터넷 연결에 문제가 아닐 수 있습니다.
     * 505 HTTP Version Not Supported
     * 	   서버에서 지원되지 않는 HTTP 버전을 클라이언트가 요청하였습니다. 대부분의 웹 브라우저는 웹 서버가 1.x 버전의 HTTP 프로토콜을 지원한다고 가정합니다. 실제로 1.0 이하의 매우 오래된 버전은 요즘 거의 사용되지 않습니다. 특히 최신 버전의 프로토콜보다 보안 및 성능이 좋지 않기 때문입니다. 따라서 웹 브라우저에서 이 오류가 표시되는 경우 웹 서버 소프트웨어에서 지원하는 HTTP 버전을 확인해 보아야 합니다.
     * 506 Variant Also Negotiates
     * 	   서버에 내부 구성 오류가 있는 경우 발생합니다. 요청을 위한 투명한 콘텐츠 협상이 순환 참조로 이어집니다.
     * 507 Insufficient Storage
     * 	   선택한 가변 리소스는 투명한 서버에 내부 구성 요류가 있는 경우 발생합니다. 콘텐츠 협상에 참여하도록 구성되므로 협상 과정에서 적절한 끝점이 아닙니다.
     * 508 Loop Detected (WebDAV)
     * 	   서버가 요청을 처리하는 동안 무한 루프를 감지한 경우 발생합니다.
     * 510 Not Extended
     * 	   서버가 요청을 이행하려면 요청에 대한 추가 확장이 필요합니다.
     * 511 Network Authentication Required
     *     상태 코드는 클라이언트가 네트워크 액세스를 얻기 위해 인증할 필요가 있음을 나타냅니다. 
     */
    
    //500 INTERNAL SERVER ERROR
    , INTERNAL_SERVER_ERROR(500, "999999", "INTERNAL SERVER ERROR")
	, NOT_IMPLEMENTE(501, "999501", "Bad Gateway")
    , BAD_GATEWAY(502, "999502", "INTERNAL SERVER ERROR")
    , SERVICE_UNAVAILABLE(503, "999503", "Service Unavailable")
    , GATEWAY_TIMEOUT(504, "999504", "Gateway Timeout")
    , HTTP_VERSION_NOT_SUPPORTED(505, "999505", "HTTP Version Not Supported")
    , VARIANT_ALSO_NEGOTIATES(506, "999506", "Variant Also Negotiates")
    , INSUFFICEIENT_STORAGE(507, "999507", "Insufficient Storage")
    , LOOP_DETECTED(508, "999508", "Loop Detected (WebDAV)")
    , NOT_EXTENDED(510, "999510", "Not Extended")
    , NETWORK_AUTHENTICATION_REQUIRED(511, "999511", "Network Authentication Required")
    ;
    
	private final int httpStatus;
	private final String resultCode;
    private final String resultMessage;
    
    public static ErrorCode valueOf(int httpStatus) {
        for (ErrorCode errorCode : ErrorCode.values()) {
          if (errorCode.httpStatus == httpStatus) {
            return errorCode;
          }
        }
        
        return null;  // or throw exception
      }
}
