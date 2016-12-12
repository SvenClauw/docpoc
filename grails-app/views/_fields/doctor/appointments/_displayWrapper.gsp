<ul>
    <g:each in="${bean.appointments}" var="appointment">
        <li>
            Date: <g:link method="GET" resource="${appointment}"><f:display bean="${appointment}"
                                                                            property="date"/></g:link>
            Patient: <g:link method="GET" resource="${appointment.patient}"><f:display bean="${appointment.patient}"
                                                                                       property="name"/></g:link>
        </li>
    </g:each>
</ul>