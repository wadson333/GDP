import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserConfiguration, NewUserConfiguration } from '../user-configuration.model';

export type PartialUpdateUserConfiguration = Partial<IUserConfiguration> & Pick<IUserConfiguration, 'id'>;

export type EntityResponseType = HttpResponse<IUserConfiguration>;
export type EntityArrayResponseType = HttpResponse<IUserConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class UserConfigurationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-configurations');

  create(userConfiguration: NewUserConfiguration): Observable<EntityResponseType> {
    return this.http.post<IUserConfiguration>(this.resourceUrl, userConfiguration, { observe: 'response' });
  }

  update(userConfiguration: IUserConfiguration): Observable<EntityResponseType> {
    return this.http.put<IUserConfiguration>(
      `${this.resourceUrl}/${this.getUserConfigurationIdentifier(userConfiguration)}`,
      userConfiguration,
      { observe: 'response' },
    );
  }

  partialUpdate(userConfiguration: PartialUpdateUserConfiguration): Observable<EntityResponseType> {
    return this.http.patch<IUserConfiguration>(
      `${this.resourceUrl}/${this.getUserConfigurationIdentifier(userConfiguration)}`,
      userConfiguration,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserConfigurationIdentifier(userConfiguration: Pick<IUserConfiguration, 'id'>): number {
    return userConfiguration.id;
  }

  compareUserConfiguration(o1: Pick<IUserConfiguration, 'id'> | null, o2: Pick<IUserConfiguration, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserConfigurationIdentifier(o1) === this.getUserConfigurationIdentifier(o2) : o1 === o2;
  }

  addUserConfigurationToCollectionIfMissing<Type extends Pick<IUserConfiguration, 'id'>>(
    userConfigurationCollection: Type[],
    ...userConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userConfigurations: Type[] = userConfigurationsToCheck.filter(isPresent);
    if (userConfigurations.length > 0) {
      const userConfigurationCollectionIdentifiers = userConfigurationCollection.map(userConfigurationItem =>
        this.getUserConfigurationIdentifier(userConfigurationItem),
      );
      const userConfigurationsToAdd = userConfigurations.filter(userConfigurationItem => {
        const userConfigurationIdentifier = this.getUserConfigurationIdentifier(userConfigurationItem);
        if (userConfigurationCollectionIdentifiers.includes(userConfigurationIdentifier)) {
          return false;
        }
        userConfigurationCollectionIdentifiers.push(userConfigurationIdentifier);
        return true;
      });
      return [...userConfigurationsToAdd, ...userConfigurationCollection];
    }
    return userConfigurationCollection;
  }
}
