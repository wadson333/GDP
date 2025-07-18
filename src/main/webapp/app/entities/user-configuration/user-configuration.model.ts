import { IUser } from 'app/entities/user/user.model';

export interface IUserConfiguration {
  id: number;
  twoFactorEnabled?: boolean | null;
  twoFactorSecret?: string | null;
  receiveEmailNotifs?: boolean | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUserConfiguration = Omit<IUserConfiguration, 'id'> & { id: null };
