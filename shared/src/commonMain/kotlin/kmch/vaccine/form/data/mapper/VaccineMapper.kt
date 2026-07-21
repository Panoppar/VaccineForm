package kmch.vaccine.form.data.mapper

import kmch.vaccine.form.data.remote.dto.LotCreateResponseDto
import kmch.vaccine.form.data.remote.dto.VaccineCreateResponseDto
import kmch.vaccine.form.domain.model.LotInfo
import kmch.vaccine.form.domain.model.VaccineInfo

fun VaccineCreateResponseDto.toDomain() = VaccineInfo(vaccineId = vaccineId)

fun LotCreateResponseDto.toDomain() = LotInfo(lotId = lotId)
