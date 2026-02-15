import { UserRole } from '../types';

const REGISTER_API_URL = 'http://localhost:8080/api/auth/register';

export interface RegisterPayload {
  username: string;
  password: string;
  email: string;
  role: UserRole;
}

export const register = async (payload: RegisterPayload): Promise<{ ok: boolean; message?: string }> => {
  try {
    const res = await fetch(REGISTER_API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      const data = await res.json().catch(() => ({}));
      const message = data?.message ?? data?.error ?? `Registration failed (${res.status})`;
      return { ok: false, message };
    }

    return { ok: true };
  } catch (err) {
    return { ok: false, message: err instanceof Error ? err.message : 'Network error' };
  }
};
