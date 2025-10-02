package boev.app.services.interfaces.units;

import boev.app.models.units.MilitaryUnit;
import boev.app.payload.units.company.CompanyDto;
import boev.app.payload.units.company.CompanyResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;

import java.util.List;

public interface CompanyService {
    List<MilitaryUnitResponse> getAllSimple();
    List<CompanyResponse> getAll();
    CompanyResponse get(long id);
    CompanyResponse delete(long id);
    CompanyResponse create(CompanyDto companyDto);
    CompanyResponse update(CompanyDto companyDto, long id);
}
