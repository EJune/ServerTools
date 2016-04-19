// AIX 6.1 or 7.1 초기 세팅  ver 1.0.0 
// last updated 2016-03-29

// test

// 최신 패치 TL 설치한다.
void OS패치() {
	IBM patch download site ://
	or 엔지니어 지원 요청
} 

// 유틸리티 설치한다.
void 툴설치() {
	ssh
	lsof
	rsync
	zip / unzip
}

// 쉘 환경을 변경
void PS2변경() {
	// 프롬프트 쉘 변경 , 쉘 vi 설정 추가
	/etc/profile 파일 마지막에 아래 추가
	============================================
	#Custom Setting for dongbulife
	export HOSTNAME=`hostname`
	export PS1='[$USER@${HOSTNAME}:$PWD] # '
	alias la='ls -a'
	alias ll='ls -alF'
	alias lr='ls -alrt'
	alias lsf='ls -aF'
	set -o vi
	===========================================	

}

// 기본 루트디렉토리 /를 변경한다
void 루트디렉토리변경() {
	mkdir /root
	chmod 700 /root
	
	cp /.sh_history /root
	cp /.vi_history /root
	

	vi /etc/passwd
	=============================================
	root:!:0:0::/root:/usr/bin/ksh
	=============================================
	
	# 재로그인
	rm /.sh_history
	rm /.vi_history
		
}

//  words 파일 복사 , 비밀번호 생성시 사용
void word파일복사() {
	words 파일 업로드
	/usr/share/dict 경로로 복사
	
	ls -l /usr/share/dict/words
	
	-rw-------    1 root     system      4953699 Oct 26 2012  words
	
}

//  sysmon 계정 , sysadmin 그룹 생성
void sysmon_sysadmin_생성() {

// sysadmin 그룹 생성
mkgroup -A id=2000 sysadmin

// sysmon 계정 생성
mkuser id='800' pgrp='sysadmin' sugroups='sysadmin' gecos='
SystemMonitoringAccount' sysmon

// 비밀번호 생성
passwd sysmon  
-- Arm

// 개인계정 생성
mkuser id='2000' pgrp='sysadmin' sugroups='sysadmin'
 gecos='EJPark' 99190
passwd 99190

mkuser id='2001' pgrp='sysadmin' sugroups='sysadmin' gecos='SHChoi' 190678
mkuser id='2002' pgrp='sysadmin' sugroups='sysadmin' gecos='HYChoi' 190595
 190595
 190678
 
 vi /etc/security/passwd
 새로 만든 계정의 flags 부분 삭제
}



// 비밀번호 복잡도 , umask 등 설정한다
// /etc/security/user
void default_계정_설정 {

// /etc/seucurity/user 파일의 default 부분을 아래와 같이 바꾼다.
default:
        admin = false
        login = true
        su = false
        daemon = true
        rlogin = true
        sugroups = ALL
        admgroups =
        ttys = ALL
        auth1 = SYSTEM
        auth2 = NONE
        tpath = nosak
        umask = 027
        expires = 0
        SYSTEM = "compat"
        logintimes = 
        pwdwarntime = 30
        account_locked = false
        loginretries = 5
        histexpire = 26
        histsize = 3
        minage = 0
        maxage = 10
        maxexpired = 1
        minalpha = 4
        minother = 1
        minlen = 8
        mindiff = 3
        maxrepeats = 8
        shell = /bin/ksh
        dictionlist = /usr/share/dict/words
        pwdchecks =	
		
		
// root 부문 maxage = 0  추가한다, rlogin = false 하면 직접 접속이 막히니 개인계정을 반드시 만들고 설정한다.
root:
        admin = true
        SYSTEM = "compat"
        registry = files
        loginretries = 0
		rlogin = false
		maxage = 0
        account_locked = false
        admgroups = sysadmin
		sugroups = sysadmin
		su = true
}


// 공통파일시스템, /app , /log 를 생성한다.
void 공통_파일시스템_생성() {
	
	각각 10G 씩 생성한다.
	// rootvg가 미러된 경우에는 lv 생성을 먼저생성
	(생성할때 미러옵션 선택), 그후 jfs2로 포맷하는게 편리하다.
	
}


// 주요파일 권한 설정을 한다.
void 중요파일_권한설정() {
	
	chmod 600 /etc/passwd
	chmod 644 /etc/hosts
	chmod 600 /etc/inetd.conf
	chmod 600 /etc/syslog.conf
	chmod 600 /etc/services
	
	chmod -s /usr/bin/X11/xlock
	chmod -s /usr/sbin/mount
	chmod -s /usr/sbin/lchangelv
	
	chown root /var/adm/cron/cron.deny
	chown root /var/adm/cron/at.deny  

}


// 불필요 서비스를 종료한다.
void 불필요서비스_종료() {
	
	vi /etc/inetd.conf
	====================
	// ssh 설치 후 telnet, ftp 등 모든 서비스를 막는다
	// 모두 주석처리
	======================
	
	//  NFS 데몬 종료
	stopsrc -g nfsd
	
	// NFS 자동기동 주석처리
	vi /etc/inittab
	======================
	:rcnfs:23456789:wait:/etc/rc.nfs > /dev/console
	2>&1 # Start NFS Daemons
	========================
	
	// sendmail  disable 아래 주석처리
	vi /etc/rc.tcpip
	===================================
	#start /usr/lib/sendmail "$src_running" "-bd -q${qpi}"
	===================================
	
}

// xntpd 설정
void xntpd() {
	
	// 아래 주석 제거 및 -X 옵션 추가
	vi /etc/rc.tcpip
	===============
	# Start up Network Time Protocol (NTP) daemon
	start /usr/sbin/xntpd -x "$src_running"
	===============
	
	
	// 아래 server 추가
	vi /etc/ntpd.conf 
	===============
	server 172.18.1.249
	server 172.18.1.248
	===============
		
}

// DNS 서버 설정
void DNS설정() {
	
	// resov.conf 생성 후 아래 내용 추가
	// 외부 DMZ 서버인 경우  210.127.58.200 
	//, 210.127.58.201 로 설정
	vi /etc/resolv.conf
	=====================
	nameserver      172.18.1.249
	nameserver      172.18.1.248
	======================
	
}




